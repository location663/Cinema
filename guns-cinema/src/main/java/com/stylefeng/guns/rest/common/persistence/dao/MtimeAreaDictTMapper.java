package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeAreaDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.ArrayList;

/**
 * <p>
 * 地域信息表 Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
public interface MtimeAreaDictTMapper extends BaseMapper<MtimeAreaDictT> {

    ArrayList<MtimeAreaDictT> getAreaConditionList();

}
