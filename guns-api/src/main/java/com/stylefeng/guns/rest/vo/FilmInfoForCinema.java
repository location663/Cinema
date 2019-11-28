package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmInfoForCinema implements Serializable {

    private Integer uuid;
    private String filmId;
    private String filmEnName;
    private String filmScore;
    private Integer filmScoreNum;
    private Integer filmLength;
    private String biography;
    private Integer directorId;
    private String filmImgs;

}
