package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.service.UserService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Reference(interfaceClass = UserService.class, check = false)
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @RequestMapping("register")
    public BaseResponVO userRegister(UserRegisterDTO user){
        BaseResponVO baseResponVO = userService.userRegister(user);
        return baseResponVO;
    }

    /**
     * 用户名验证
     * @param username
     * @return
     */
    @RequestMapping("check")
    public BaseResponVO userCheck(String username){
        BaseResponVO baseResponVO = userService.userCheck(username);
        return baseResponVO;
    }

    /**用户退出
     * @return
     */
    @RequestMapping("logout")
    public BaseResponVO userLogout() throws CinemaException {
        String token = HttpKit.getRequest().getHeader("Authorization").substring(7);
        BaseResponVO baseResponVO = userService.userLogout(token);
        return baseResponVO;
    }

    /**用户信息查询
     * @return
     */
    @RequestMapping("getUserInfo")
    public BaseResponVO getUserInfo() throws CinemaException {
        String token = HttpKit.getRequest().getHeader("Authorization").substring(7);
        BaseResponVO baseResponVO = userService.getUserInfo(token);
        return baseResponVO;
    }

    @RequestMapping("updateUserInfo")
    public BaseResponVO updateUserInfo(@RequestBody UserVO userVO) throws CinemaException {
        String token = HttpKit.getRequest().getHeader("Authorization").substring(7);
        BaseResponVO baseResponVO = userService.updateUserInfo(token, userVO);
        return baseResponVO;
    }
}

