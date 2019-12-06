package com.stylefeng.guns.rest.modular.auth.util;

import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenUtil {

    private final String TOKEN_HEAD = "Bearer ";

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    public Integer getUserId(HttpServletRequest request, HttpServletResponse response) throws CinemaException {
        final String header = request.getHeader(jwtProperties.getHeader());
        if (header != null && header.startsWith(TOKEN_HEAD)) {
            UserVO userVO = (UserVO) redisTemplate.opsForValue().get(header.substring(7));
            if (null == userVO) {
                throw new CinemaException(1, "用户尚未登录");
            }
            Integer uuid = userVO.getUuid();
            return uuid;
        }
        return null;
    }
}
