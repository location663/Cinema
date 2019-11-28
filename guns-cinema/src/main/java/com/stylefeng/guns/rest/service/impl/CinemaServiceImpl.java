package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.GetCinemasVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Override
    public BaseResponVO getCinemasList(Integer brandId, Integer hallType, Integer districtId,Integer pageSize,Integer nowPage) {
        BaseResponVO baseResponVO = new BaseResponVO();
        EntityWrapper<MtimeCinemaT> mtimeCinemaTEntityWrapper = new EntityWrapper<>();
        if (brandId != 99){
            mtimeCinemaTEntityWrapper.eq("brand_id", brandId);
        }
        if (hallType != 99){
            mtimeCinemaTEntityWrapper.like("hall_ids", "#" + hallType + "#");
        }
        if (districtId != 99){
            mtimeCinemaTEntityWrapper.eq("area_id",districtId);
        }
        Page<MtimeCinemaT> mtimeCinemaTPage = new Page<>();
        List<Map<String, Object>> listMaps = mtimeCinemaTMapper.selectMapsPage(mtimeCinemaTPage, mtimeCinemaTEntityWrapper);
        List<GetCinemasVo> cinemasVOs = trans2Films(listMaps);
        baseResponVO.setData(cinemasVOs);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setMsg("");
        baseResponVO.setNowPage(nowPage);
        baseResponVO.setStatus(0);
        baseResponVO.setTotalPage((int) Math.ceil(1.0*listMaps.size()/pageSize));
        return baseResponVO;
    }

    /**
     *
     * @param brandId
     * @param hallType  这个就是hallTypeId 用于置true
     * @param areaId
     * @return
     */
    @Override
    public BaseResponVO getConditionList(Integer brandId,Integer hallType,Integer areaId) {
        BaseResponVO baseResponVO = new BaseResponVO();
        ArrayList<MtimeAreaDictT> areaList = mtimeCinemaTMapper.getAreaConditionList();
        ArrayList<MtimeBrandDictT> brandList = mtimeCinemaTMapper.getBrandConditionList();
        ArrayList<MtimeHallDictT> halltypeList = mtimeCinemaTMapper.getHallCondition();
        for (MtimeAreaDictT mtimeAreaDictT : areaList) {
            if (mtimeAreaDictT.getAreaId().equals(areaId)){
                mtimeAreaDictT.setActive(true);
            }else {
                mtimeAreaDictT.setActive(false);
            }
        }
        for (MtimeBrandDictT mtimeBrandDictT : brandList) {
            if (mtimeBrandDictT.getBrandId().equals(brandId)){
                mtimeBrandDictT.setActive(true);
            }else {
                mtimeBrandDictT.setActive(false);
            }
        }
        for (MtimeHallDictT mtimeHallDictT : halltypeList) {
            if (mtimeHallDictT.getHalltypeId().equals(hallType)){
                mtimeHallDictT.setActive(true);
            }else {
                mtimeHallDictT.setActive(false);
            }
        }
        ArrayList<List> lists = new ArrayList<>();
        lists.add(areaList);
        lists.add(brandList);
        lists.add(halltypeList);
        baseResponVO.setData(lists);
        baseResponVO.setImgPre("");
        baseResponVO.setStatus(0);
        return baseResponVO;
    }


    /**
     * 将查询到的 List<Map<String, Object>> 转换成list
     * @param listMaps
     * @return
     */
    private List<GetCinemasVo> trans2Films(List<Map<String, Object>> listMaps) {
        ArrayList<GetCinemasVo> cinemasVOs = new ArrayList<>();
        for (Map<String, Object> listMap : listMaps) {
            GetCinemasVo getCinemasVo = new GetCinemasVo();
            getCinemasVo.setCinemaAddress((String) listMap.get("cinemaAddress"));
            getCinemasVo.setCinamaName((String) listMap.get("cinemaName"));
            getCinemasVo.setMinimumPrice((Integer) listMap.get("minimumPrice"));
            getCinemasVo.setUuid((Integer) listMap.get("uuid"));
            cinemasVOs.add(getCinemasVo);
        }
        return cinemasVOs;
    }

}
