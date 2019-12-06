package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.modular.auth.util.TokenUtil;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.promo.PromoOrder;
import com.stylefeng.guns.rest.vo.promo.PromoStatusEnum;
import com.stylefeng.guns.rest.vo.promo.PromoStockLogStatusEnum;
import com.stylefeng.guns.rest.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("promo")
@Slf4j
public class PromoController {

    @Reference(interfaceClass = PromoService.class, check = false)
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenUtil tokenUtil;

    private final String PROMO_TOKEN_PROFIX = "PROMO_TOKEN_PROFIX_%s_%s";

    private RateLimiter rateLimiter;



    @PostConstruct
    public void init(){
        rateLimiter = RateLimiter.create(10);
    }


    @RequestMapping("getPromo")
    public BaseResponVO getPromo(@Valid CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException {
        return promoService.getPromo(cinemaGetCinemasVO);
    }

    @RequestMapping("publishPromoStock")
    public BaseResponVO publishPromoStock(){
        return promoService.publishPromoStock();
    }

    /*@RequestMapping("createOrder")
    public BaseResponVO createOrder(@Valid PromoOrder promoOrder) throws CinemaException {
        Integer promoId = promoOrder.getPromoId();
        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId.toString());
        if (stock < promoOrder.getAmount()){
            return new ErrorResponVO(1, "订单数不合法");
        }
        if (null == promoOrder.getPromoToken()){
            return new BaseResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
        }
        String authorization = HttpKit.getRequest().getHeader("Authorization");
        String token = authorization.substring(7);
        promoOrder.setToken(token);
        return promoService.createOrder(promoOrder);
    }*/

    /**
     * mq事务实现秒杀下单，扣减库存
     * @param promoId
     * @param amount
     * @param promoToken
     * @return
     * @throws CinemaException
     */
    @RequestMapping("createOrder")
    public BaseResponVO createOrderInTransaction(Integer promoId, Integer amount,
                                                 String promoToken, HttpServletRequest request,
                                                 HttpServletResponse response) throws CinemaException {

        Integer userId = tokenUtil.getUserId(request, response);
        if (null == userId){
            return new BaseResponVO(1, "还未登录");
        }
        if (null == promoToken){
            return new BaseResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
        }
        String tokenKey = String.format(PROMO_TOKEN_PROFIX, promoId, userId);
        String tokenFromRedis = (String) redisTemplate.opsForValue().get(tokenKey);
        if (null == tokenFromRedis) {
            return new ErrorResponVO(1, "没有令牌");
        }
        if (!tokenFromRedis.equals(promoToken)) {
            return new ErrorResponVO(1, "令牌失效");
        }

        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId.toString());
        if (stock < amount){
            return new ErrorResponVO(1, "库存不足");
        }

//        String authorization = HttpKit.getRequest().getHeader("Authorization");
//        String token = authorization.substring(7);



        // 下单前，生成一条订单流水，为了后期事务回滚
        String stockLogId = promoService.insertStockLog(promoId, amount);
        if (StringUtils.isEmpty(stockLogId)){
            return new BaseResponVO(1, "下单失败");
        }

        Boolean result = promoService.insertOrderInTransaction(promoId, amount, userId, stockLogId);
        if (!result){
            return new BaseResponVO(1, "下单失败");
        }
        return new BaseResponVO(0, "下单成功");
    }

    /**
     * 获取秒杀令牌
     * @param promoId
     * @param request
     * @param response
     * @return
     * @throws CinemaException
     */
    @RequestMapping("generateToken")
    public BaseResponVO generateToken(@RequestParam(value = "promoId", required = true) Integer promoId,
                                      HttpServletRequest request, HttpServletResponse response) throws CinemaException {
        /**
         * 根据promoId判断
         * 1. 活动是否存在
         * 2. 活动是否有效
         * 3. 活动是否正在进行
         */
        Integer status = promoService.getPromoStatusById(promoId);
        if (null == status){
            return new BaseResponVO(1, "活动不存在");
        }
        if (PromoStatusEnum.PROMO_OVER.getValue() == status){
            return new BaseResponVO(1, "活动已结束");
        }
        if (PromoStatusEnum.PROMO_NOT_BEGIN.getValue() == status){
            return new BaseResponVO(1, "活动未开始");
        }

        Integer userId = tokenUtil.getUserId(request, response);
        if (null == userId){
            return new BaseResponVO(1, "还未登录");
        }

        String tokenAmountKey = String.format("PROMO_TOKEN_OF_PROMOID_%s", promoId);
        Long increment = redisTemplate.opsForValue().increment(tokenAmountKey, -1);
        if (increment < 0){
            redisTemplate.opsForValue().increment(tokenAmountKey, 1);
            return new BaseResponVO(1, "令牌已空");
        }

        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId.toString());
        if (stock == 0){
            return new ErrorResponVO(1, "库存不足");
        }


//        double acquire = rateLimiter.acquire(1);
//        if (acquire >= 0) {
//            String tokenKey = String.format(PROMO_TOKEN_PROFIX, promoId, userId);
//            String token = UUID.randomUUID().toString().replaceAll("-", "");
//            redisTemplate.opsForValue().set(tokenKey, token);
//            redisTemplate.expire(tokenKey, 30, TimeUnit.MINUTES);
//
//            BaseResponVO baseResponVO = new BaseResponVO();
//            baseResponVO.setData(token);
//            baseResponVO.setStatus(0);
//
//            return baseResponVO;
//        }
        /**
         * com.google.common.util.concurrent包下的 RateLimiter 令牌桶限流
         */
        // tryAcquire() 在一定时间内如果没有拿到令牌，则返回false
        boolean getAcquire = rateLimiter.tryAcquire(1, 10, TimeUnit.SECONDS);
        if (getAcquire) {
            log.info("用户id为 {} 拿到令牌的时间为  {}", userId, DateUtil.getTime());
            String tokenKey = String.format(PROMO_TOKEN_PROFIX, promoId, userId);
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            redisTemplate.opsForValue().set(tokenKey, token);
            redisTemplate.expire(tokenKey, 30, TimeUnit.MINUTES);

            BaseResponVO baseResponVO = new BaseResponVO();
            baseResponVO.setMsg(token);
            baseResponVO.setStatus(0);

            return baseResponVO;
        }
        return new BaseResponVO(1, "获取令牌失败");
    }
}
