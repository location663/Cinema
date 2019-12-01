package com.stylefeng.guns.rest.service;


import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.UserVO;

public interface UserService {
    BaseResponVO userRegister(UserRegisterDTO user);

    BaseResponVO userCheck(String username);

    UserVO auth(String username, String password);


}
