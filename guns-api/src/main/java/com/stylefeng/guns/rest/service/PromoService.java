package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetConditionVO;

public interface PromoService {
    BaseResponVO getPromo(CinemaGetConditionVO cinemaGetConditionVO);
}
