/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 17:57
 **/
package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CinemaGetConditionVO {

    @Min(value = 1,message = "品牌参数出错")
    private Integer brandId = 99;

    @Min(value = 1,message = "影厅参数出错")
    private Integer hallType = 99;

    @Min(value = 1,message = "区域参数出错")
    private Integer areaId = 99;

}
