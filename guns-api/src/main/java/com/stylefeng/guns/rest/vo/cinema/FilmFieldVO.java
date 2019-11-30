/**
 * Created by IntelliJ IDEA.
 * User: Jql
 * Date  2019/11/29
 * Time  下午 3:46
 */

package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmFieldVO implements Serializable {

    private Integer fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private Integer price;

    public FilmFieldVO() {
    }

    public FilmFieldVO(Integer fieldId, String beginTime, String endTime, String language, String hallName, Integer price) {
        this.fieldId = fieldId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.language = language;
        this.hallName = hallName;
        this.price = price;
    }
}
