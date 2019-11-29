package com.stylefeng.guns.rest.exception;

public enum CinemaExceptionEnum {

    PARAMETER_ERROR(800, "参数异常"),

    USER_AUTH_ERROR(999,"系统出现异常，请联系管理员"),


    TOKEN_ERROR(1, "退出失败，用户尚未登陆"),


    Business_ERROR(1,"业务异常"),


    CINEMA_QUERY_ERROR(1, "影院信息查询失败");

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
