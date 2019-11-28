package com.stylefeng.guns.rest.common.exception;

public enum CinemaExceptionEnum {

    PARAMETER_ERROR(800, "参数异常");


    private Integer status;
    private String msg;

    CinemaExceptionEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
