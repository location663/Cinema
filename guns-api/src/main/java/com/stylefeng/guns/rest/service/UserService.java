package com.stylefeng.guns.rest.service;


import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.vo.BaseResponVO;

public interface UserService {
    BaseResponVO userRegister(UserRegisterDTO user);

    BaseResponVO userCheck(String username);

    boolean auth(String username, String password);
}
