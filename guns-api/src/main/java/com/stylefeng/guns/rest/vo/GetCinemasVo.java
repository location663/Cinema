/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 21:05
 **/
package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetCinemasVo implements Serializable {

    private String cinemaAddress;
    private String cinemaName;
    private Integer minimumPrice;
    private Integer uuid;
}
