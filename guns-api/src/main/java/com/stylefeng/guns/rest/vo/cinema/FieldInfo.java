/**
 * Created by IntelliJ IDEA.
 * User: Jql
 * Date  2019/11/28
 * Time  下午 9:23
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FieldInfo implements Serializable {
    private FilmInfoVO filmInfo;

    private CinemaInfoVO cinemaInfo;

    private HallInfoVO hallInfo;

    private List<FilmInfoVO> filmList;



    public FieldInfo() {
    }

    public FieldInfo(FilmInfoVO filmInfo, CinemaInfoVO cinemaInfo, HallInfoVO hallInfo, List<FilmInfoVO> filmList) {
        this.filmInfo = filmInfo;
        this.cinemaInfo = cinemaInfo;
        this.hallInfo = hallInfo;
        this.filmList = filmList;
    }
}
