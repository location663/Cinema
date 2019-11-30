/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 11:33
 **/
package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class CinemaConditionT implements Serializable {

    private ArrayList<MtimeAreaDictT> areaList;
    private ArrayList<MtimeBrandDictT> brandList;
    ArrayList<MtimeHallDictT> halltypeList;
}
