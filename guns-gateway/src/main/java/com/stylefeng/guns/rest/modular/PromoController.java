package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetConditionVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("promo")
public class PromoController {

    @Reference(interfaceClass = PromoService.class, check = false)
    private PromoService promoService;

    @RequestMapping("getPromo")
    public BaseResponVO getPromo(@Valid CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException {
        return promoService.getPromo(cinemaGetCinemasVO);
    }

}
