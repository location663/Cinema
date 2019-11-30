package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 品牌信息表
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
@TableName("mtime_brand_dict_t")
public class MtimeBrandDictT extends Model<MtimeBrandDictT> {

    private static final long serialVersionUID = 1L;


    /**
     * 主键编号
     */
    @TableId(value = "UUID", type = IdType.AUTO)
    private Integer brandId;
    /**
     * 显示名称
     */
    @TableField("show_name")
    private String brandName;


    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    @Override
    protected Serializable pkVal() {
        return this.brandId;
    }

    @Override
    public String toString() {
        return "MtimeBrandDictT{" +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
