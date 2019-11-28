package com.stylefeng.guns.rest.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {

    private String username;

    private String password;

    private String email;

    private String mobile;

    private String address;

}
