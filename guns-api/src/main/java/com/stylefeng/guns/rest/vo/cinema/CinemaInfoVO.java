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
public class CinemaInfoVO implements Serializable {
    private Integer cinemaId;
    private String imgUrl;
    private String cinemaName;
    private String cinemaAdress;
    private String cinemaPhone;

    public CinemaInfoVO() {
    }

    public CinemaInfoVO(Integer cinemaId, String imgUrl, String cinemaName, String cinemaAdress, String cinemaPhone) {
        this.cinemaId = cinemaId;
        this.imgUrl = imgUrl;
        this.cinemaName = cinemaName;
        this.cinemaAdress = cinemaAdress;
        this.cinemaPhone = cinemaPhone;
    }
}
