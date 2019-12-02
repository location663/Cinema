/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 13:00
 **/
package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class CinemaGetCinemasVO implements Serializable {
    @Min(value = 1,message = "品牌参数出错")
    private Integer brandId = 99;

    @Min(value = 1,message = "影厅参数出错")
    private Integer hallType = 99;

    @Min(value = 1,message = "区域参数出错")
    private Integer areaId = 99;

    @Min(value = 1,message = "页面参数出错")
    private Integer pageSize = 12;

    @Min(value = 1,message = "当前页参数出错")
    private Integer nowPage = 1;
}
