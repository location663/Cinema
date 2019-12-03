package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.vo.BaseResponVO;

public interface PayService {
    BaseResponVO getPayInfo(Integer orderId) throws CinemaParameterException;

    BaseResponVO getPayResult(Integer orderId) throws CinemaParameterException;
}
