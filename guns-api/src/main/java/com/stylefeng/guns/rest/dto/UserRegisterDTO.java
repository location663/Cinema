package com.stylefeng.guns.rest.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {

    private String username;

    private String password;

    private String email;

    private String mobile;

    private String address;

}
