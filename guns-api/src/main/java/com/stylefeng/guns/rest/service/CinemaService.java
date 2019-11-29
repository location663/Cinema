package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.common.exception.CinemaQueryFailException;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;

public interface CinemaService {

    FieldInfo getFieldInfo(Integer cinemaId, Integer fieldId );

    FieldInfo getFields(Integer cinemaId) throws CinemaQueryFailException;
}
