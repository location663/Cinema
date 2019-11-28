/**
 * Created by Intellij IDEA
 * User:cookie
 * Date:2019/11/27
 * Time:20:35
 **/
package com.stylefeng.guns.rest.vo.film;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmInfo implements Serializable {
    private Integer filmId;
    private int filmType;
    private String imgAddress;
    private String fileName;
    private String filmScore;
    private int  expectNum;
    private String showTime;
    private int  boxNum;
    public FilmInfo() {
    }
    public FilmInfo(Integer filmId, int filmType, String imgAddress, String fileName, String filmScore, int expectNum, String showTime, int boxNum) {
        this.filmId = filmId;
        this.filmType = filmType;
        this.imgAddress = imgAddress;
        this.fileName = fileName;
        this.filmScore = filmScore;
        this.expectNum = expectNum;
        this.showTime = showTime;
        this.boxNum = boxNum;
    }
}
