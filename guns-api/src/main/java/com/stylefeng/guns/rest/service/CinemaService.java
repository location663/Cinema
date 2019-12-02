package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.common.exception.CinemaQueryFailException;
import com.stylefeng.guns.rest.vo.BaseResponVO;

import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;

import com.stylefeng.guns.rest.vo.cinema.CinemaNameAndFilmIdVO;

import com.stylefeng.guns.rest.vo.cinema.FieldInfo;

public interface CinemaService {

    BaseResponVO getCinemasList(CinemaGetCinemasVO cinemaGetCinemasVO);

    BaseResponVO getConditionList(Integer brandId,Integer hallType,Integer areaId);

    FieldInfo getFieldInfo(Integer cinemaId, Integer fieldId );

    FieldInfo getFields(Integer cinemaId) throws CinemaQueryFailException;

    CinemaNameAndFilmIdVO getCinemaNameAndFilmIdByFieldId(Integer fieldId);
}
