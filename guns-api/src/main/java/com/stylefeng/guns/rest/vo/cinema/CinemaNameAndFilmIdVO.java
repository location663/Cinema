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
    private Integer filmId;
    private String beginTime;
    private String endTime;
    private Integer hallId;
    private String hallName;
    private Integer price;

    private String cinemaName;
    private String seatAddress;

    public CinemaNameAndFilmIdVO() {
    }

    public CinemaNameAndFilmIdVO(Integer cinemaId, Integer filmId, String beginTime, String endTime, Integer hallId, String hallName, Integer price, String cinemaName, String seatAddress) {
        this.cinemaId = cinemaId;
        this.filmId = filmId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.hallId = hallId;
        this.hallName = hallName;
        this.price = price;
        this.cinemaName = cinemaName;
        this.seatAddress = seatAddress;
    }
}
