package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-12-03
 */
public interface MtimePromoMapper extends BaseMapper<MtimePromo> {

    @Select("SELECT status FROM mtime_promo WHERE uuid = #{uuid} ")
    Integer selectStatusById(@Param("uuid") Integer uuid);
}
