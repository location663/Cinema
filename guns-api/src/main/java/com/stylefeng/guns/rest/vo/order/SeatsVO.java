package com.stylefeng.guns.rest.vo.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SeatsVO implements Serializable {
    private Integer limit;
    private String ids;
//    private List<List<SeatVO>> single;
//    private List<List<SeatVO>> couple;
    private SeatVO[][] single;
    private SeatVO[][] couple;
}
