/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 17:35
 **/
package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallTypeVO implements Serializable {

    private Integer halltypeId;

    private String halltypeName;

    private boolean active;

}
