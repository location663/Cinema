package com.stylefeng.guns.rest.vo;

import lombok.Data;

import javax.validation.constraints.Digits;
import java.io.Serializable;


@Data
public class FilmRequestVO implements Serializable {
    @Digits(integer = 1, fraction = 0, message = "参数异常")
    private Integer showType;
    @Digits(integer = 1, fraction = 0, message = "参数异常")
    private Integer sortId;
    @Digits(integer = 2, fraction = 0, message = "参数异常")
    private Integer catId;
    @Digits(integer = 2, fraction = 0, message = "参数异常")
    private Integer sourceId;
    @Digits(integer = 2, fraction = 0, message = "参数异常")
    private Integer yearId;
    @Digits(integer = 2, fraction = 0, message = "参数异常")
    private Integer nowPage;
    @Digits(integer = 2, fraction = 0, message = "参数异常")
    private Integer pageSize;
}
