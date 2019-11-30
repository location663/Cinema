package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 地域信息表
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
@TableName("mtime_hall_dict_t")
public class MtimeHallDictT extends Model<MtimeHallDictT> {

    private static final long serialVersionUID = 1L;


    /**
     * 主键编号
     */
    @TableId(value = "UUID", type = IdType.AUTO)
    private Integer halltypeId;
    /**
     * 显示名称
     */
    @TableField("show_name")
    private String halltypeName;
    /**
     * 座位文件存放地址
     */
    @TableField("seat_address")
    private String seatAddress;


    public Integer getHalltypeId() {
        return halltypeId;
    }

    public void setHalltypeId(Integer halltypeId) {
        this.halltypeId = halltypeId;
    }

    public String getHalltypeName() {
        return halltypeName;
    }

    public void setHalltypeName(String halltypeName) {
        this.halltypeName = halltypeName;
    }

    public String getSeatAddress() {
        return seatAddress;
    }

    public void setSeatAddress(String seatAddress) {
        this.seatAddress = seatAddress;
    }

    @Override
    protected Serializable pkVal() {
        return this.halltypeId;
    }

    @Override
    public String toString() {
        return "MtimeHallDictT{" +
                ", halltypeId=" + halltypeId +
                ", halltypeName='" + halltypeName + '\'' +
                ", seatAddress='" + seatAddress + '\'' +
                '}';
    }
}
