/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 17:30
 **/
package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaVO implements Serializable {

    private Integer areaId;

    private String areaName;

    private boolean active;
}
