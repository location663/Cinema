package com.stylefeng.guns.rest.vo.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderInfoVO implements Serializable {
    private String cinemaName;
    private String fieldTime;
    private String filmName;
    private String orderId;
    private String orderPrice;
    private String orderStatus;
    private String orderTimestamp;
    private String seatsName;
    private Double filmPrice;
    private Integer quantity;
    private String orderMsg;
}
