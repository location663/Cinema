/**
 * Created by IntelliJ IDEA
 * User: DB
 * Date:
 * Time: 11:33
 **/
package com.stylefeng.guns.rest.common.persistence.model;

import com.stylefeng.guns.rest.vo.cinema.AreaVO;
import com.stylefeng.guns.rest.vo.cinema.BrandVO;
import com.stylefeng.guns.rest.vo.cinema.HallTypeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class CinemaConditionT implements Serializable {

    private ArrayList<AreaVO> areaList;
    private ArrayList<BrandVO> brandList;
    private ArrayList<HallTypeVO> halltypeList;
}
