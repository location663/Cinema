/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/27
 * Time:20:44
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmIndexVo implements Serializable {
    private List<BannerVo> banners;
    private FilmVo hotFilms;
    private FilmVo soonFilms;
    private List<FilmInfo> boxRanking;
    private List<FilmInfo> expectRanking;
    private List<FilmInfo> top100;
}
