package com.stylefeng.guns.rest.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuyTicketDTO implements Serializable {
    private Integer fieldId;
    private Integer soldSeats;
    private String seatsName;
}
