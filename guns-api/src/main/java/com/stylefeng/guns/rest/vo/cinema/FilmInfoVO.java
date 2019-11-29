/**
 * 影院模块
 * 4、获取场次详细信息接口
 * User: Jql
 * Date  2019/11/28
 * Time  下午 8:41
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmInfoVO implements Serializable {
    private Integer filmId;
    private String filmName;
    private Integer filmType;
    private String imgAddress;
    private String filmCats;
    private Integer filmLength;

    public FilmInfoVO() {
    }

    public FilmInfoVO(Integer filmId, String filmName, Integer filmType, String imgAddress, String filmCats, Integer filmLength) {
        this.filmId = filmId;
        this.filmName = filmName;
        this.filmType = filmType;
        this.imgAddress = imgAddress;
        this.filmCats = filmCats;
        this.filmLength = filmLength;
    }
}
