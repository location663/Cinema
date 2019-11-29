package com.stylefeng.guns.rest.common.aop;

import com.stylefeng.guns.core.aop.BaseControllerExceptionHandler;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.common.exception.CinemaParameterException;
import com.stylefeng.guns.rest.common.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午3:19:56
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 拦截jwt相关异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorTip jwtException(JwtException e) {
        return new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage());
    }

    @ExceptionHandler(CinemaParameterException.class)
    @ResponseBody
    public ErrorResponVO cinemaParameterException(CinemaParameterException e){
        return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
    }

    @ExceptionHandler(CinemaException.class)
    @ResponseBody
    public ErrorResponVO cinemaException(CinemaException e){
        return new ErrorResponVO(e.getStatus(), e.getMsg());

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponVO systemException(Exception e){
        return new ErrorResponVO(CinemaExceptionEnum.SYSTEM_ERROR.getStatus(), CinemaExceptionEnum.SYSTEM_ERROR.getMsg());
    }
}
