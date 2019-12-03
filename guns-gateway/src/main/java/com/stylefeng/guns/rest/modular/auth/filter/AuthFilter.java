package com.stylefeng.guns.rest.modular.auth.filter;


import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
public class AuthFilter extends OncePerRequestFilter {



    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String ignore = jwtProperties.getIgnore();
        String[] split = ignore.split(",");
        String servletPath = request.getServletPath();

        for (String path : split) {
            if (servletPath.contains(path)){
                chain.doFilter(request, response);
                return;
            }
        }

//        if (request.getServletPath().equals("/" + jwtProperties.getAuthPath())) {
//            chain.doFilter(request, response);
//            return;
//        }
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            try {
                UserVO user = (UserVO) redisTemplate.opsForValue().get(authToken);
                if (user == null) {
                    // o为null 时说明token已过期
//                    RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(), BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
                    RenderUtil.renderJson(response, new ErrorResponVO(700,"未登录"));
//                    response.sendRedirect("http://localhost:1818/login");

                    return;
                }
<<<<<<< HEAD
//              刷新时间
                redisTemplate.expire(authToken,5 * 60, TimeUnit.SECONDS);
=======
                redisTemplate.expire(authToken,5 * 60 * 60 * 24, TimeUnit.SECONDS);
                redisTemplate.expire(user.getUserName(),5 * 60 * 60 * 24, TimeUnit.SECONDS);
>>>>>>> b0a58c22bc02e64b6d47dc55993f20ddcdcb1e57
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new ErrorResponVO(700,"未登录"));
//                RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
//                response.setHeader("refresh","2;url=http://localhost:1818/login");

                return;
            }

        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, new ErrorResponVO(700,"未登录"));
//            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
//            response.setHeader("refresh","2;url=http://localhost:1818/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
