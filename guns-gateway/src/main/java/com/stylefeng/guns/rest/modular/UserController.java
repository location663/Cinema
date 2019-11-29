package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.service.UserService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    UserService userService;

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

}

