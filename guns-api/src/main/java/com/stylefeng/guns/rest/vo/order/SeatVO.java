package com.stylefeng.guns.rest.vo.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeatVO implements Serializable {
    private Integer seatId;
    private Integer row;
    private Integer column;
}
