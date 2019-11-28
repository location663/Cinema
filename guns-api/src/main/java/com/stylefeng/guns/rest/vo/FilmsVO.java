package com.stylefeng.guns.rest.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmsVO implements Serializable {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
}
