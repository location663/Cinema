package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.promo.PromoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("promo")
public class PromoController {

    @Reference(interfaceClass = PromoService.class, check = false)
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @RequestMapping("getPromo")
//    public BaseResponVO getPromo() throws CinemaParameterException {
//        CinemaGetCinemasVO cinemaGetCinemasVO = new CinemaGetCinemasVO();
//        cinemaGetCinemasVO.setAreaId(99);
//        cinemaGetCinemasVO.setBrandId(99);
//        cinemaGetCinemasVO.setHallType(99);
//        cinemaGetCinemasVO.setNowPage(1);
//        cinemaGetCinemasVO.setPageSize(12);
//        return promoService.getPromo(cinemaGetCinemasVO);
//    }

    @RequestMapping("getPromo")
    public BaseResponVO getPromo(@Valid CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException {
        return promoService.getPromo(cinemaGetCinemasVO);
    }

    @RequestMapping("publishPromoStock")
    public BaseResponVO publishPromoStock(){
        return promoService.publishPromoStock();
    }

    @RequestMapping("createOrder")
    public BaseResponVO createOrder(@Valid PromoOrder promoOrder) throws CinemaException {
        Integer promoId = promoOrder.getPromoId();
        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId.toString());
        if (stock < promoOrder.getAmount()){
            return new ErrorResponVO(1, "订单数不合法");
        }
        if (null == promoOrder.getPromoToken()){
            return new BaseResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
        }
        return promoService.createOrder(promoOrder);
    }
}
