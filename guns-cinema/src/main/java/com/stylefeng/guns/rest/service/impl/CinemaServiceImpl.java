package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.exception.CinemaQueryFailException;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallFilmInfoTMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.GetCinemasVo;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.ActorVO;
import com.stylefeng.guns.rest.vo.FilmForCinema;
import com.stylefeng.guns.rest.vo.FilmInfoForCinema;
import com.stylefeng.guns.rest.vo.cinema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.Map;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Reference(interfaceClass = FilmService.class,check = false)
    private FilmService filmService;

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimeAreaDictTMapper mtimeAreaDictTMapper;

    @Autowired
    MtimeBrandDictTMapper mtimeBrandDictTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Override
    public BaseResponVO getCinemasList(Integer brandId, Integer hallType, Integer areaId,Integer pageSize,Integer nowPage) {
        BaseResponVO baseResponVO = new BaseResponVO();
        EntityWrapper<MtimeCinemaT> mtimeCinemaTEntityWrapper = new EntityWrapper<>();
        if (brandId != 99){
            mtimeCinemaTEntityWrapper.eq("brand_id", brandId);
        }
        if (hallType != 99){
            mtimeCinemaTEntityWrapper.like("hall_ids", "#" + hallType + "#");
        }
        if (areaId != 99){
            mtimeCinemaTEntityWrapper.eq("area_id",areaId);
        }
        Page<MtimeCinemaT> mtimeCinemaTPage = new Page<>();
        List<Map<String, Object>> listMaps = mtimeCinemaTMapper.selectMapsPage(mtimeCinemaTPage, mtimeCinemaTEntityWrapper);
        List<GetCinemasVo> cinemasVOs = trans2Films(listMaps);
        baseResponVO.setData(cinemasVOs);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setMsg("");
        baseResponVO.setNowPage(nowPage);
        baseResponVO.setStatus(0);
        baseResponVO.setTotalPage((int) Math.ceil(1.0*cinemasVOs.size()/pageSize));
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
        ArrayList<MtimeAreaDictT> areaList = mtimeAreaDictTMapper.getAreaConditionList();
        ArrayList<MtimeBrandDictT> brandList = mtimeBrandDictTMapper.getBrandConditionList();
        ArrayList<MtimeHallDictT> halltypeList = mtimeHallDictTMapper.getHallCondition();
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
        CinemaConditionT cinemaConditionT = new CinemaConditionT();
        cinemaConditionT.setAreaList(areaList);
        cinemaConditionT.setBrandList(brandList);
        cinemaConditionT.setHalltypeList(halltypeList);
        baseResponVO.setData(cinemaConditionT);
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
            getCinemasVo.setCinemaName((String) listMap.get("cinemaName"));     // *******
            getCinemasVo.setMinimumPrice((Integer) listMap.get("minimumPrice"));
            getCinemasVo.setUuid((Integer) listMap.get("uuid"));
            cinemasVOs.add(getCinemasVo);
        }
        return cinemasVOs;
    }


    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @Override
    public FieldInfo getFieldInfo(Integer cinemaId, Integer fieldId) {
        FieldInfo fieldInfo = new FieldInfo();

        //根据放映场次ID获取放映信息
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
        FilmForCinema filmByFilmId = filmService.getFilmByFilmId(mtimeFieldT.getFilmId());
        FilmInfoForCinema filmInfoByFilmId = filmService.getFilmInfoByFilmId(mtimeFieldT.getFilmId());//同上

        //封装filmInfo
        FilmInfoVO filmInfoVO = new FilmInfoVO();
        filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
        filmInfoVO.setFilmName(filmByFilmId.getFilmName());
        filmInfoVO.setFilmType(filmByFilmId.getFilmType());
        filmInfoVO.setImgAddress(filmByFilmId.getImgAddress());
        filmInfoVO.setFilmCats(filmByFilmId.getFilmCats());
        filmInfoVO.setFilmLength(filmInfoByFilmId.getFilmLength());

        //封装cinemaInfo
        CinemaInfoVO cinemaInfoVO = getCinemaInfoVOByCinemaId(cinemaId);

        //封装hallInfo
        HallInfoVO hallInfoVO = new HallInfoVO();
        MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());//
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

    //封装cinemaInfo
    private CinemaInfoVO getCinemaInfoVOByCinemaId(Integer cinemaId) {
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);//根据影院编号获取影院对象信息
        if (mtimeCinemaT!=null){
            cinemaInfoVO.setCinemaId(mtimeCinemaT.getUuid());//
            cinemaInfoVO.setCinemaName(mtimeCinemaT.getCinemaName());
            cinemaInfoVO.setCinemaAdress(mtimeCinemaT.getCinemaAddress());
            cinemaInfoVO.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
            cinemaInfoVO.setImgUrl(mtimeCinemaT.getImgAddress());
        }
        return cinemaInfoVO;
    }


    /**
     * 获取播放场次接口
     * @param cinemaId
     * @return
     */
    @Override
    public FieldInfo getFields(Integer cinemaId) throws CinemaQueryFailException {
        FieldInfo fieldInfo = new FieldInfo();

        //封装 cinemaInfo
        CinemaInfoVO cinemaInfoVO = getCinemaInfoVOByCinemaId(cinemaId);

        //根据影院ID获取该影院所有场次
        EntityWrapper<MtimeFieldT> fieldTEntityWrapper = new EntityWrapper<MtimeFieldT>();
        fieldTEntityWrapper.eq("cinema_id",cinemaId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(fieldTEntityWrapper);
        if (mtimeFieldTS == null){
            throw new CinemaQueryFailException();
        }

        //封装 filmList
        List<FilmInfoVO> filmList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mtimeFieldTS)){  //判断mtimeFieldTS 非空
            Set set = new HashSet();
            for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
                if (set.add(mtimeFieldT.getFilmId())) {
                    //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
                    FilmForCinema filmByFilmId = filmService.getFilmByFilmId(mtimeFieldT.getFilmId());
                    FilmInfoForCinema filmInfoByFilmId = filmService.getFilmInfoByFilmId(mtimeFieldT.getFilmId());//同上

                    if (filmByFilmId.getUuid() != null) {
                        //封装filmInfo
                        FilmInfoVO filmInfoVO = new FilmInfoVO();
                        filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
                        filmInfoVO.setFilmName(filmByFilmId.getFilmName());
                        filmInfoVO.setFilmType(filmByFilmId.getFilmType());
                        filmInfoVO.setImgAddress(filmByFilmId.getImgAddress());

                        EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
                        hallFilmInfoTEntityWrapper.eq("film_id",mtimeFieldT.getFilmId());
                        List<MtimeHallFilmInfoT> mtimeHallFilmInfoTS = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
                        if (!CollectionUtils.isEmpty(mtimeHallFilmInfoTS)){
                            filmInfoVO.setFilmCats(mtimeHallFilmInfoTS.get(0).getFilmCats());
                        }
                        filmInfoVO.setFilmLength(filmInfoByFilmId.getFilmLength());

                        List<ActorVO> actorVOS = filmService.listActorVOByFilmId(mtimeFieldT.getFilmId());
                        StringBuilder stringBuilder = new StringBuilder();
                        if (!CollectionUtils.isEmpty(actorVOS)) {
                            for (ActorVO actorVO : actorVOS) {
                                stringBuilder.append(actorVO.getDirectorName()).append(",");
                            }
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            filmInfoVO.setActors(stringBuilder.toString());
                        }
                        filmList.add(filmInfoVO);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(filmList)) {  //判断mtimeFieldTS 非空
                for (FilmInfoVO filmInfoVO : filmList) {
                    List<FilmFieldVO> list = new ArrayList<>();
                    filmInfoVO.setFilmFields(list);  //先设置这个属性为一个空List
                }
            }

            for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
                //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
//                FilmForCinema filmByFilmId = filmService.getFilmByFilmId(mtimeFieldT.getFilmId());
//                FilmInfoForCinema filmInfoByFilmId = filmService.getFilmInfoByFilmId(mtimeFieldT.getFilmId());//同上

                //根据场次id获取 filmField
                //MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
                FilmFieldVO filmFieldVO = new FilmFieldVO();
                filmFieldVO.setBeginTime(mtimeFieldT.getBeginTime());
                filmFieldVO.setEndTime(mtimeFieldT.getEndTime());
                filmFieldVO.setFieldId(mtimeFieldT.getHallId());
                filmFieldVO.setHallName(mtimeFieldT.getHallName());
                filmFieldVO.setPrice(mtimeFieldT.getPrice());
                EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
                hallFilmInfoTEntityWrapper.eq("film_id",mtimeFieldT.getFilmId());
                List<MtimeHallFilmInfoT> mtimeHallFilmInfoTS = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
                if (!CollectionUtils.isEmpty(mtimeHallFilmInfoTS)){
                    filmFieldVO.setLanguage(mtimeHallFilmInfoTS.get(0).getFilmLanguage());
                }

                if (!CollectionUtils.isEmpty(filmList)) {  //判断mtimeFieldTS 非空
                    for (FilmInfoVO filmInfoVO : filmList) {
                        if (filmInfoVO.getFilmId().equals(mtimeFieldT.getFilmId())){
                            filmInfoVO.getFilmFields().add(filmFieldVO);//封装 filmFields
                        }
                    }
                }
                //封装filmInfo
                /*FilmInfoVO filmInfoVO = new FilmInfoVO();
                filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
                filmInfoVO.setFilmName(filmByFilmId.getFilmName());
                filmInfoVO.setFilmType(filmByFilmId.getFilmType());
                filmInfoVO.setImgAddress(filmByFilmId.getImgAddress());
                filmInfoVO.setFilmCats(filmByFilmId.getFilmCats());
                filmInfoVO.setFilmLength(filmInfoByFilmId.getFilmLength());

                List<ActorVO> actorVOS = filmService.listActorVOByFilmId(mtimeFieldT.getFilmId());
                StringBuilder stringBuilder = new StringBuilder();
                for (ActorVO actorVO : actorVOS) {
                    stringBuilder.append(actorVO.getDirectorName()).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                filmInfoVO.setActors(stringBuilder.toString());*/

                //filmInfoVO.getFilmFields().add(filmFieldVO);//封装 filmFields
                //filmList.add(filmInfoVO);
            }
        }

        fieldInfo.setCinemaInfo(cinemaInfoVO);
        fieldInfo.setFilmList(filmList);
        return fieldInfo;
    }


    /**
     * 根据场次id获取影院name和影片id
     * @param fieldId
     * @return
     */
    @Override
    public CinemaNameAndFilmIdVO getCinemaNameAndFilmIdByFieldId(Integer fieldId) {
        CinemaNameAndFilmIdVO cinemaNameAndFilmNameVO = new CinemaNameAndFilmIdVO();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);  //根据场次id获取场次对象
        if (mtimeFieldT != null){
            cinemaNameAndFilmNameVO.setPrice(mtimeFieldT.getPrice());
            cinemaNameAndFilmNameVO.setFilmId(mtimeFieldT.getFilmId());   //封装影片id
            MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(mtimeFieldT.getCinemaId());//根据影院id获取影院对象
            if (mtimeCinemaT != null){
                cinemaNameAndFilmNameVO.setCinemaName(mtimeCinemaT.getCinemaName()); //封装影院name
            }

        }

        return cinemaNameAndFilmNameVO;
    }
}
