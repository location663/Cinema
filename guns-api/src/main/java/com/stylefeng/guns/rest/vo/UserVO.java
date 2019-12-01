package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private String userName;
    private String nickName;
    private String userPhone;
    private String address;
}
