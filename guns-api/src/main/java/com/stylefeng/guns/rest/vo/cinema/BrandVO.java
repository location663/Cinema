/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 17:33
 **/
package com.stylefeng.guns.rest.vo.cinema;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVO implements Serializable {

    private Integer brandId;

    private String brandName;

    private boolean active;

}
