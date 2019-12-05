package com.stylefeng.guns.rest.vo.promo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PromoInfoVO implements Serializable {
    private String cinemaAddress;
    private Integer cinemaId;
    private String cinemaName;
    private String description;
    private String startTime;
    private String endTime;
    private String imgAddress;
    private Double price;
    private Integer status;
    private Integer stock;
    private Integer uuid;
}
