package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.common.exception.CinemaParameterException;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.rest.service.UserService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Resource(name = "simpleValidator")
//    private IReqValidator reqValidator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference(interfaceClass = UserService.class, check = false)
    private UserService userService;

    @RequestMapping(value = "${jwt.auth-path}")
    public BaseResponVO createAuthenticationToken(AuthRequest authRequest) {

//        // 验证账号密码
//        boolean validate = reqValidator.validate(authRequest);

        UserVO userVO = userService.auth(authRequest.getUserName(),authRequest.getPassword());

        if (userVO == null){
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }

        // 生成randomKey
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);

        redisTemplate.opsForValue().set(token,userVO);
        redisTemplate.expire(token,5 * 60, TimeUnit.SECONDS);

        ResponseEntity<AuthResponse> ok = ResponseEntity.ok(new AuthResponse(token, randomKey));
        AuthResponse body = ok.getBody();

        BaseResponVO baseResponVO = new BaseResponVO(0,null,body,null,null,null);
        return baseResponVO;
    }
}
