package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-12-03
 */
public interface MtimeStockLogMapper extends BaseMapper<MtimeStockLog> {

    @Update("UPDATE mtime_stock_log SET status = #{status} WHERE uuid = #{uuid} ")
    Integer updateStatusByUuid(@Param("uuid") String uuid, @Param("status") Integer status);

    @Select("SELECT status FROM mtime_stock_log WHERE uuid = #{uuid} ")
    Integer selectStatusByUuid(@Param("uuid") String uuid);

    @Insert("INSERT INTO mtime_stock_log (uuid, promo_id, amount, status) values (#{uuid} , #{promoId} , #{amount} , #{status} )")
    Integer insertStockLog(@Param("uuid") String uuid,
                           @Param("promoId") Integer promoId,
                           @Param("amount") Integer amount,
                           @Param("status") Integer status );
}
