package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-11-30
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {

    @Select("SELECT DISTINCT LAST_INSERT_ID() FROM mooc_order_t")
    Integer selectLastInsertId();
}
