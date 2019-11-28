package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;

import java.util.ArrayList;

/**
 * <p>
 * 影院信息表 Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
public interface MtimeCinemaTMapper extends BaseMapper<MtimeCinemaT> {

    ArrayList<MtimeAreaDictT> getAreaConditionList();

    ArrayList<MtimeBrandDictT> getBrandConditionList();

    ArrayList<MtimeHallDictT> getHallCondition();

}
