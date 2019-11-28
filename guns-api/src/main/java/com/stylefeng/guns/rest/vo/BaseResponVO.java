package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponVO implements Serializable {
    private Integer status;
    private String imgPre;
    private Object data;
    private Integer nowPage;
    private Integer totalPage;
    private String msg;

    public BaseResponVO() {
    }

    public BaseResponVO(Integer status, String imgPre, Object data, Integer nowPage, Integer totalPage, String msg) {
        this.status = status;
        this.imgPre = imgPre;
        this.data = data;
        this.nowPage = nowPage;
        this.totalPage = totalPage;
        this.msg = msg;
    }

    public BaseResponVO(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
