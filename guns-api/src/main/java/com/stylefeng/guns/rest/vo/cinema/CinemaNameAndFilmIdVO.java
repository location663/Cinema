/**
 * Created by IntelliJ IDEA.
 * User: Jql
 * Date  2019/11/30
 * Time  下午 4:19
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaNameAndFilmIdVO implements Serializable {

    private Integer cinemaId;

    private String cinemaName;

    private Integer filmId;

    private Integer price;



    public CinemaNameAndFilmIdVO() {
    }

    public CinemaNameAndFilmIdVO(Integer cinemaId, String cinemaName, Integer filmId, Integer price) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.filmId = filmId;
        this.price = price;
    }
}
