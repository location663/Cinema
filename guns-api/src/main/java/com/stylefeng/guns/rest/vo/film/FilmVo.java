/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/27
 * Time:20:41
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class FilmVo implements Serializable {
    private int filmNum;
    private int nowPage;
    private int totalPage;
    private List<FilmInfo> filmInfo;
}
