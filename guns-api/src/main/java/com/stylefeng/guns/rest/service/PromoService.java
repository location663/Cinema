package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.promo.PromoOrder;

public interface PromoService {
    BaseResponVO getPromo(CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException;

    BaseResponVO publishPromoStock();

    BaseResponVO createOrder(PromoOrder promoOrder) throws CinemaException;
}
