package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;

public interface PromoService {
    BaseResponVO getPromo(CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException;
}
