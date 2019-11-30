package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeBrandDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.vo.cinema.BrandVO;

import java.util.ArrayList;

/**
 * <p>
 * 品牌信息表 Mapper 接口
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
public interface MtimeBrandDictTMapper extends BaseMapper<MtimeBrandDictT> {

    ArrayList<BrandVO> getBrandConditionList();
}
