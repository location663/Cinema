package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.bean.PromoProducer;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaPartVO;
import com.stylefeng.guns.rest.vo.promo.PromoInfoVO;
import com.stylefeng.guns.rest.vo.promo.PromoOrder;
import com.stylefeng.guns.rest.vo.promo.PromoStockLogStatusEnum;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = PromoService.class)
public class PromoServiceImpl implements PromoService {

    @Reference(interfaceClass = CinemaService.class, check = false)
    private CinemaService cinemaService;

    @Autowired
    private MtimePromoMapper promoMapper;

    @Autowired
    private MtimePromoStockMapper promoStockMapper;

    @Autowired
    private MtimePromoOrderMapper promoOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PromoProducer producer;

    @Autowired
    private MtimeStockLogMapper stockLogMapper;

    private ExecutorService executorService;

    private final String PROMO_TOKEN_PROFIX = "PROMO_TOKEN_OF_PROMOID_%s";

    private final int PROMO_TIMES = 5;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(100);
    }

    /**
     * 获取秒杀活动的分页信息
     *
     * @param cinemaGetCinemasVO
     * @return
     * @throws CinemaParameterException
     */
    @Override
    public BaseResponVO getPromo(@RequestParam(required = false) CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException {

//        List<CinemaPartVO> partOfCinemasValue = cinemaService.getPartOfCinemasValue(cinemaGetCinemasVO);
//        if (partOfCinemasValue.isEmpty()){
//            return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
//        }
//        ArrayList<PromoInfoVO> promoInfoVOS = new ArrayList<>();
//        for (CinemaPartVO cinemaPartVO : partOfCinemasValue) {
//            PromoInfoVO promoInfo = getPromoInfo(cinemaPartVO);
//            if (null != promoInfo) {
//                promoInfoVOS.add(promoInfo);
//            }
//        }
//        BaseResponVO baseResponVO = new BaseResponVO();
//        baseResponVO.setData(promoInfoVOS);
//        baseResponVO.setStatus(0);
//        return baseResponVO;



        EntityWrapper<MtimePromo> mtimePromoEntityWrapper = new EntityWrapper<>();
        if (cinemaGetCinemasVO.getBrandId() != null && cinemaGetCinemasVO.getBrandId() != 99) {
            mtimePromoEntityWrapper.eq("cinema_id", cinemaGetCinemasVO.getBrandId());
        }
        Integer[] arr = new Integer[3];
        arr[0] = 0;
        arr[1] = 1;
        mtimePromoEntityWrapper.in("status", arr);
        List<MtimePromo> mtimePromos = promoMapper.selectList(mtimePromoEntityWrapper);
        ArrayList<PromoInfoVO> promoInfoVOS = new ArrayList<>();
        for (MtimePromo mtimePromo : mtimePromos) {
            CinemaPartVO cinemaPartVO = cinemaService.getCinemaInfoById(mtimePromo.getCinemaId());
            PromoInfoVO promoInfoVO = new PromoInfoVO();
            BeanUtils.copyProperties(cinemaPartVO, promoInfoVO);
            BigDecimal price = mtimePromo.getPrice();
            promoInfoVO.setPrice(price.doubleValue());
            promoInfoVO.setDescription(mtimePromo.getDescription());
            promoInfoVO.setStartTime(TransferUtils.parseDate2String(mtimePromo.getStartTime()));
            promoInfoVO.setEndTime(TransferUtils.parseDate2String(mtimePromo.getEndTime()));
            promoInfoVO.setStatus(mtimePromo.getStatus());
            promoInfoVOS.add(promoInfoVO);
            EntityWrapper<MtimePromoStock> promoStockEntityWrapper = new EntityWrapper<>();
            promoStockEntityWrapper.eq("promo_id", mtimePromo.getUuid());
            List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(promoStockEntityWrapper);
            if (mtimePromoStocks.isEmpty()) {
                throw new CinemaParameterException();
            }
            MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
            promoInfoVO.setStock(mtimePromoStock.getStock());
            promoInfoVO.setUuid(mtimePromo.getUuid());
        }
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(promoInfoVOS);
        baseResponVO.setStatus(0);
        return baseResponVO;
    }

    /**
     * 将数据库中的秒杀库存缓存到redis
     *
     * @return
     */
    @Override
    public BaseResponVO publishPromoStock() {
        EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
        Integer[] arr = new Integer[3];
        arr[0] = 0;
        arr[1] = 1;
        promoEntityWrapper.in("status", arr);
        List<MtimePromo> mtimePromos = promoMapper.selectList(promoEntityWrapper);
        if (!mtimePromos.isEmpty()) {
            for (MtimePromo mtimePromo : mtimePromos) {
                EntityWrapper<MtimePromoStock> promoStockEntityWrapper = new EntityWrapper<>();
                promoStockEntityWrapper.eq("promo_id", mtimePromo.getUuid());
                List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(promoStockEntityWrapper);
                if (mtimePromoStocks.isEmpty()) {
                    return new ErrorResponVO(CinemaExceptionEnum.USER_AUTH_ERROR.getStatus(), CinemaExceptionEnum.USER_AUTH_ERROR.getMsg());
                }
                MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
                Integer promoId = mtimePromoStock.getPromoId();
                Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId.toString());
                if (null == stock) {
                    // 将数据库中的库存同步到redis中
                    redisTemplate.opsForValue().set("promoId" + promoId.toString(), mtimePromoStock.getStock());
                    redisTemplate.expire("promoId" + promoId.toString(), 30, TimeUnit.DAYS);
                    // 存入秒杀令牌初始数量
                    String tokenKey = String.format(PROMO_TOKEN_PROFIX, promoId);
                    redisTemplate.opsForValue().set(tokenKey, "" + stock * PROMO_TIMES);
                }
            }

        }
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setStatus(0);
        baseResponVO.setMsg("发布成功");
        return baseResponVO;
    }

    /**
     * 创建秒杀订单
     *
     * @param promoOrder
     * @return
     * @throws CinemaException
     */
    @Override
    @Transactional
    public BaseResponVO createOrder(PromoOrder promoOrder) throws CinemaException {

        MtimePromo mtimePromo = promoMapper.selectById(promoOrder.getPromoId());
//        EntityWrapper<MtimePromoStock> mtimePromoStockEntityWrapper = new EntityWrapper<>();
//        mtimePromoStockEntityWrapper.eq("promo_id", promoOrder.getPromoId());
//        List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(mtimePromoStockEntityWrapper);
//        if (mtimePromoStocks.isEmpty()){
//            return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
//        }
//        MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
//        if (mtimePromoStock.getStock() < promoOrder.getAmount()){
//            throw new CinemaException(1, "库存不足");
//        }
//        mtimePromoStock.setStock(mtimePromoStock.getStock() - promoOrder.getAmount());
//        Integer integer = promoStockMapper.updateById(mtimePromoStock);
//        if (0 == integer) {
//            throw new CinemaException(1, "库存不足");
//        }

        /**
         * 在redis中扣减库存
         */
        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoOrder.getPromoId().toString());
        if (null == stock) {
            publishPromoStock();
        }
//        redisTemplate.opsForValue().set("promoId" + promoOrder.getPromoId().toString(), stock - promoOrder.getAmount());
//        redisTemplate.expire("promoId" + promoOrder.getPromoId().toString(), 30, TimeUnit.DAYS);

        redisTemplate.opsForValue().increment("promoId" + promoOrder.getPromoId().toString(), -1 * promoOrder.getAmount());


        /**
         * 发出消息，扣减mysql的库存
         */
        producer.decreaseStock(promoOrder.getPromoId(), promoOrder.getAmount());

        UserVO user = (UserVO) redisTemplate.opsForValue().get(promoOrder.getToken());
        MtimePromoOrder mtimePromoOrder = new MtimePromoOrder();
        mtimePromoOrder.setUserId(user.getUuid());
        mtimePromoOrder.setCinemaId(mtimePromo.getCinemaId());
        mtimePromoOrder.setAmount(promoOrder.getAmount());
        @Min(value = 1) Integer amount = promoOrder.getAmount();
        BigDecimal price = mtimePromo.getPrice();
        double i = price.doubleValue() * amount;
        mtimePromoOrder.setPrice(new BigDecimal(i));
        mtimePromoOrder.setStartTime(mtimePromo.getStartTime());
        mtimePromoOrder.setCreateTime(new Date());
        mtimePromoOrder.setEndTime(mtimePromo.getEndTime());
        mtimePromoOrder.setExchangeCode("aaa");
        Integer insert = promoOrderMapper.insert(mtimePromoOrder);

        if (insert == 1) {
            return new BaseResponVO(0, "下单成功");
        }
        return new BaseResponVO(1, "下单失败");
    }

    private PromoInfoVO getPromoInfo(CinemaPartVO cinemaPartVO) throws CinemaParameterException {
        PromoInfoVO promoInfoVO = new PromoInfoVO();
        EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
        promoEntityWrapper.eq("cinema_id", cinemaPartVO.getCinemaId());
        List<MtimePromo> mtimePromos = promoMapper.selectList(promoEntityWrapper);

        if (mtimePromos.isEmpty()) {
            return null;
        }
        MtimePromo mtimePromo = mtimePromos.get(0);

        BeanUtils.copyProperties(cinemaPartVO, promoInfoVO);
        BigDecimal price = mtimePromo.getPrice();
        promoInfoVO.setPrice(price.doubleValue());
        promoInfoVO.setDescription(mtimePromo.getDescription());
        promoInfoVO.setStartTime(TransferUtils.parseDate2String(mtimePromo.getStartTime()));
        promoInfoVO.setEndTime(TransferUtils.parseDate2String(mtimePromo.getEndTime()));
        promoInfoVO.setStatus(mtimePromo.getStatus());
        EntityWrapper<MtimePromoStock> promoStockEntityWrapper = new EntityWrapper<>();
        promoStockEntityWrapper.eq("promo_id", mtimePromo.getUuid());
        List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(promoStockEntityWrapper);
        if (mtimePromoStocks.isEmpty()) {
            throw new CinemaParameterException();
        }
        MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
        promoInfoVO.setStock(mtimePromoStock.getStock());
        promoInfoVO.setUuid(mtimePromo.getUuid());
        return promoInfoVO;
    }

    @Override
    public String insertStockLog(Integer promoId, Integer amount) {
        MtimeStockLog mtimeStockLog = new MtimeStockLog();
        mtimeStockLog.setAmount(amount);
        mtimeStockLog.setPromoId(promoId);
        mtimeStockLog.setStatus(PromoStockLogStatusEnum.STOCK_LOG_INIT.getValue());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 18);
        mtimeStockLog.setUuid(uuid);
//        Integer insert = stockLogMapper.insert(mtimeStockLog);
        Integer insert = stockLogMapper.insertStockLog(uuid, promoId, amount, PromoStockLogStatusEnum.STOCK_LOG_INIT.getValue());
        if (null != insert) {
            return uuid;
        }
        return null;
    }

    @Override
    public Boolean insertOrderInTransaction(Integer promoId, Integer amount, Integer userId, String stockLogId) {
        Boolean orderInTransaction = producer.createOrderInTransaction(promoId, amount, userId, stockLogId);
        return orderInTransaction;
    }

    @Transactional
    @Override
    public Boolean createOrder4Producer(Integer promoId, Integer amount, Integer userId, String stockLogId) throws CinemaException {
        MtimePromo mtimePromo = promoMapper.selectById(promoId);
        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId);

        if (null == stock) {
            publishPromoStock();
        }
        // 库存不足
        if (stock < amount) {

            executorService.submit(() -> {
                stockLogMapper.updateStatusByUuid(stockLogId, PromoStockLogStatusEnum.STOCK_LOG_FAILED.getValue());
            });

            throw new CinemaException(1, "库存不足");

        }
        redisTemplate.opsForValue().increment("promoId" + promoId, -1 * amount);
        MtimePromoOrder mtimePromoOrder = new MtimePromoOrder();
        mtimePromoOrder.setUserId(userId);
        mtimePromoOrder.setCinemaId(mtimePromo.getCinemaId());
        mtimePromoOrder.setAmount(amount);
        BigDecimal price = mtimePromo.getPrice();
        double i = price.doubleValue() * amount;
        mtimePromoOrder.setPrice(new BigDecimal(i));
        mtimePromoOrder.setStartTime(mtimePromo.getStartTime());
        mtimePromoOrder.setCreateTime(new Date());
        mtimePromoOrder.setEndTime(mtimePromo.getEndTime());
        mtimePromoOrder.setExchangeCode("aaa");
        Integer insert = promoOrderMapper.insert(mtimePromoOrder);

        if (insert < 1) {
            // 异步线程更改流水表的订单状态
            executorService.submit(() -> {
                stockLogMapper.updateStatusByUuid(stockLogId, PromoStockLogStatusEnum.STOCK_LOG_FAILED.getValue());
            });
            throw new CinemaException(CinemaExceptionEnum.USER_AUTH_ERROR.getStatus(), CinemaExceptionEnum.USER_AUTH_ERROR.getMsg());
        }

        stockLogMapper.updateStatusByUuid(stockLogId, PromoStockLogStatusEnum.STOCK_LOG_SUCCESS.getValue());
        return true;
    }

    @Override
    public Integer getStockLogStatusByUuid(String uuid) {
        Integer integer = stockLogMapper.selectStatusByUuid(uuid);
        return integer;
    }

    @Override
    public Integer updateStockByPromoId(Integer promoId, Integer amount) {
        Integer integer = promoStockMapper.updateStockByPromoId(amount, promoId);
        return integer;
    }

    @Override
    public Integer getPromoStatusById(Integer promoId) {
        return promoMapper.selectStatusById(promoId);
    }
}
