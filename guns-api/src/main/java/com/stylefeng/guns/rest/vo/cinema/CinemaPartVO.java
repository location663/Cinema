/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 16:37
 **/
package com.stylefeng.guns.rest.vo.cinema;


import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaPartVO implements Serializable {

    private String cinemaAddress;

    private Integer cinemaId;

    private String cinemaName;

    private String imgAddress;

}
