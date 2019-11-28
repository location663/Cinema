package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.cinema.CinemaInfoVO;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;
import com.stylefeng.guns.rest.vo.cinema.FilmInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {
    @Reference
    private FilmService filmService;

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;


    @Override
    public FieldInfo getFieldInfo(Integer cinemaId, Integer fieldId) {
        FieldInfo fieldInfo = new FieldInfo();

//        FilmInfoVO cinema_filmInfoVO =getFilmInfo(cinemaId,fieldId);
//        CinemaInfoVO cinema_cinemaInfoVO=getCinemaInfo(cinemaId,fieldId);
//        HallInfoVO cinema_hallInfoVO=getHallInfo(cinemaId,fieldId);

        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);//根据放映场次ID获取放映信息
        //根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
        FilmInfoVO filmInfoVO = new FilmInfoVO();
        filmInfoVO.setFilmId(mtimeFieldT.getFilmId());
        filmInfoVO.setFilmName();
        filmInfoVO.setFilmType();
        filmInfoVO.setImgAddress();
        filmInfoVO.setFilmCats();
        filmInfoVO.setFilmLength();



        //封装cinemaInfo
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);//根据影院编号获取影院信息
        cinemaInfoVO.setCinemaId(mtimeCinemaT.getUuid());
        cinemaInfoVO.setCinemaName(mtimeCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaAdress(mtimeCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
        cinemaInfoVO.setImgUrl(mtimeCinemaT.getImgAddress());


        fieldInfo.setFilmInfo(cinema_filmInfoVO);
        fieldInfo.setCinemaInfo(cinemaInfoVO);
        fieldInfo.setHallInfo(cinema_hallInfoVO);

//        BaseResponVO baseResponVO = new BaseResponVO();
//        baseResponVO.setData();
//        baseResponVO.setStatus();
//        baseResponVO.setImgPre();

        return cinema_fieldInfo;
    }
}
