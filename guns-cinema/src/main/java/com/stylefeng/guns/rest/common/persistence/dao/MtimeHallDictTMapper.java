package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
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
public interface MtimeHallDictTMapper extends BaseMapper<MtimeHallDictT> {

    ArrayList<MtimeHallDictT> getHallCondition();
}
