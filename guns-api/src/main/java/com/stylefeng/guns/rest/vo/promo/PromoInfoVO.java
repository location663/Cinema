package com.stylefeng.guns.rest.vo.promo;

import lombok.Data;

@Data
public class PromoInfoVO {
    private String cinemaAddress;
    private Integer cinemaId;
    private String cinameName;
    private String description;
    private String startTime;
    private String endTime;
    private String imgAddress;
    private Double price;
    private Integer status;
    private Integer stock;
    private Integer uuid;
}
