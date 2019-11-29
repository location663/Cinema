/**
 * 影院模块
 * 4、获取场次详细信息接口
 * User: Jql
 * Date  2019/11/28
 * Time  下午 8:42
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;
@Data
public class HallInfoVO implements Serializable {
    private Integer hallFieldId;
    private String hallName;
    private Integer price;
    private String seatFile;
    private String soldSeats; //已售座位

    public HallInfoVO() {
    }

    public HallInfoVO(Integer hallFieldId, String hallName, Integer price, String seatFile, String soldSeats) {
        this.hallFieldId = hallFieldId;
        this.hallName = hallName;
        this.price = price;
        this.seatFile = seatFile;
        this.soldSeats = soldSeats;
    }
}
