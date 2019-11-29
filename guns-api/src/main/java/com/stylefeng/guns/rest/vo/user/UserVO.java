package com.stylefeng.guns.rest.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {
    private Integer uuid;
    private String userName;
    private String userPwd;
    private String nickName;
    private Integer userSex;
    private String birthday;
    private String email;
    private String userPhone;
    private String address;
    private String headUrl;
    private String biography;
    private Integer lifeState;
    private Date beginTime;
    private Date updateTime;
}
