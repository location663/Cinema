/**
 * Created by IntelliJ IDEA.
 * User: Jql
 * Date  2019/11/28
 * Time  下午 9:23
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldInfo implements Serializable {
    private FilmInfoVO filmInfo;

    private CinemaInfoVO cinemaInfo;

    private HallInfoVO hallInfo;

    public FieldInfo() {
    }

    public FieldInfo(FilmInfoVO filmInfo, CinemaInfoVO cinemaInfo, HallInfoVO hallInfo) {
        this.filmInfo = filmInfo;
        this.cinemaInfo = cinemaInfo;
        this.hallInfo = hallInfo;
    }
}
