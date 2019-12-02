package com.stylefeng.guns.rest.service;


import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;


public interface UserService {

    BaseResponVO userRegister(UserRegisterDTO user);

    BaseResponVO userCheck(String username);

    UserVO auth(String username, String password);

    BaseResponVO userLogout(String token) throws CinemaException;

    BaseResponVO getUserInfo(String token) throws CinemaException;

    BaseResponVO updateUserInfo(String token, UserVO userVO) throws CinemaException;

}
