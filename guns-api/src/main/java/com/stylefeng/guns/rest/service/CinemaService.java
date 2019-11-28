package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;

public interface CinemaService {

    BaseResponVO getCinemasList(Integer brandId, Integer hallType, Integer districtId,Integer pageSize,Integer nowPage);

    BaseResponVO getConditionList(Integer brandId,Integer hallType,Integer areaId);

}
