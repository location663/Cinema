package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallFilmInfoT;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.FilmForCinema;
import com.stylefeng.guns.rest.vo.FilmInfoForCinema;
import com.stylefeng.guns.rest.vo.cinema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Reference(interfaceClass = FilmService.class,check = false)
    private FilmService filmService;

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;
    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;
    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;



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

    //封装cinemaInfo
    private CinemaInfoVO getCinemaInfoVOByCinemaId(Integer cinemaId) {
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);//根据影院编号获取影院对象信息
        cinemaInfoVO.setCinemaId(mtimeCinemaT.getUuid());
        cinemaInfoVO.setCinemaName(mtimeCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaAdress(mtimeCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
        cinemaInfoVO.setImgUrl(mtimeCinemaT.getImgAddress());
        return cinemaInfoVO;
    }


    /**
     * 获取播放场次接口
     * @param cinemaId
     * @return
     */
    @Override
    public FieldInfo getFields(Integer cinemaId) {
        FieldInfo fieldInfo = new FieldInfo();

        //封装 cinemaInfo
        CinemaInfoVO cinemaInfoVO = getCinemaInfoVOByCinemaId(cinemaId);

        //根据影院ID获取该影院所有场次
        EntityWrapper<MtimeFieldT> fieldTEntityWrapper = new EntityWrapper<MtimeFieldT>();
        fieldTEntityWrapper.eq("cinema_id",cinemaId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(fieldTEntityWrapper);

        //封装 filmList
        List<FilmInfoVO> filmList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mtimeFieldTS)){  //判断mtimeFieldTS 非空

            for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {

                //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
                FilmForCinema filmByFilmId = filmService.getFilmByFilmId(mtimeFieldT.getFilmId());
                FilmInfoForCinema filmInfoByFilmId = filmService.getFilmInfoByFilmId(mtimeFieldT.getFilmId());//同上

                //根据场次id获取 filmField
                MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
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

                //封装filmInfo
                FilmInfoVO filmInfoVO = new FilmInfoVO();
                filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
                filmInfoVO.setFilmName(filmByFilmId.getFilmName());
                filmInfoVO.setFilmType(filmByFilmId.getFilmType());
                filmInfoVO.setImgAddress(filmByFilmId.getImgAddress());
                filmInfoVO.setFilmCats(filmByFilmId.getFilmCats());
                filmInfoVO.setFilmLength(filmInfoByFilmId.getFilmLength());

                
                filmInfoVO.setActors();
                filmInfoVO.getFilmFields().add(filmFieldVO);//封装 filmFields
                filmList.add(filmInfoVO);
            }
        }

        fieldInfo.setCinemaInfo(cinemaInfoVO);
        fieldInfo.setFilmList(filmList);
        return fieldInfo;
    }


}
