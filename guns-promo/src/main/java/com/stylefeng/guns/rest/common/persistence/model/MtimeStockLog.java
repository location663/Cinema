package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ali
 * @since 2019-12-03
 */
@TableName("mtime_stock_log")
public class MtimeStockLog extends Model<MtimeStockLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("uuid")
    private String uuid;
    /**
     * 秒杀活动id
     */
    @TableField("promo_id")
    private Integer promoId;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 状态
     */
    private Integer status;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "MtimeStockLog{" +
        "uuid=" + uuid +
        ", promoId=" + promoId +
        ", amount=" + amount +
        ", status=" + status +
        "}";
    }
}
