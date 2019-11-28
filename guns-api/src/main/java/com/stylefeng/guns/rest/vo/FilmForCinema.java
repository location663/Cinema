package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FilmForCinema implements Serializable {

    private Integer uuid;
    private String filmName;
    private Integer filmType;
    private String imgAddress;
    private String filmScore;
    private Integer filmPresalenum;
    private Integer filmBoxOffice;
    private Integer filmSource;
    private String filmCats;
    private Integer filmArea;
    private Integer filmDate;
    private Date filmTime;
    private Integer filmStatus;

}
