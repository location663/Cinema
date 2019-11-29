package com.stylefeng.guns.rest.common.exception;

import lombok.Data;

@Data
public class CinemaException extends Exception {
    private Integer status;
    private String msg;



    public CinemaException(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
