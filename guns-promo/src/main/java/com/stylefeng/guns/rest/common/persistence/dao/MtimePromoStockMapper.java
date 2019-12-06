package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-12-03
 */
public interface MtimePromoStockMapper extends BaseMapper<MtimePromoStock> {
    @Update("UPDATE mtime_promo_stock SET stock = stock - #{amount} WHERE promo_id = #{promoId} ")
    public Integer updateStockByPromoId(@Param("amount") Integer amount, @Param("promoId") Integer promoId);
}
