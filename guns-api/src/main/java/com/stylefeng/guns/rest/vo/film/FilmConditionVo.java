/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/28
 * Time:22:36
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.util.List;
@Data
public class FilmConditionVo {
    private List<CatVo> catInfo;
    private List<SourceVo> sourceInfo;
    private List<YearVo> yearInfo;
}
