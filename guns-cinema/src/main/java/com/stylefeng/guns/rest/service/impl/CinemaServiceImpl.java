package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
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
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.FilmForCinema;
import com.stylefeng.guns.rest.vo.FilmInfoForCinema;
import com.stylefeng.guns.rest.vo.cinema.CinemaInfoVO;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;
import com.stylefeng.guns.rest.vo.cinema.FilmInfoVO;
import com.stylefeng.guns.rest.vo.cinema.HallInfoVO;
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

    @Reference
    private FilmService filmService;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;



    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @Override
    public FieldInfo getFieldInfo(Integer cinemaId, Integer fieldId) {
        FieldInfo fieldInfo = new FieldInfo();

        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);//根据放映场次ID获取放映信息

        //封装filmInfo
        FilmForCinema filmByFilmId = filmService.getFilmByFilmId(fieldId); //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
        FilmInfoForCinema filmInfoByFilmId = filmService.getFilmInfoByFilmId(fieldId);//同上
        FilmInfoVO filmInfoVO = new FilmInfoVO();
        filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
        filmInfoVO.setFilmName(filmByFilmId.getFilmName());
        filmInfoVO.setFilmType(filmByFilmId.getFilmType());
        filmInfoVO.setImgAddress(filmByFilmId.getImgAddress());
        filmInfoVO.setFilmCats(filmByFilmId.getFilmCats());
        filmInfoVO.setFilmLength(filmInfoByFilmId.getFilmLength());

        //封装cinemaInfo
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);//根据影院编号获取影院信息
        cinemaInfoVO.setCinemaId(mtimeCinemaT.getUuid());
        cinemaInfoVO.setCinemaName(mtimeCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaAdress(mtimeCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
        cinemaInfoVO.setImgUrl(mtimeCinemaT.getImgAddress());

        //封装hallInfo
        HallInfoVO hallInfoVO = new HallInfoVO();
        MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
        hallInfoVO.setHallFieldId(mtimeFieldT.getHallId());
        hallInfoVO.setHallName(mtimeFieldT.getHallName());
        hallInfoVO.setPrice(mtimeFieldT.getPrice());
        hallInfoVO.setSeatFile(mtimeHallDictT.getSeatAddress());
        hallInfoVO.setSoldSeats(null);  //(需要改进SoldSeats,根据订单查看已售座位) 根据放映场次ID获取已售座位

        fieldInfo.setFilmInfo(filmInfoVO);
        fieldInfo.setCinemaInfo(cinemaInfoVO);
        fieldInfo.setHallInfo(hallInfoVO);

        return fieldInfo;
    }
}
