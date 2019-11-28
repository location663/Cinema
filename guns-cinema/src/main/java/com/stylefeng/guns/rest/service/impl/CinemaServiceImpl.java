package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.FilmForCinema;
import com.stylefeng.guns.rest.vo.FilmInfoForCinema;
import com.stylefeng.guns.rest.vo.cinema.CinemaInfoVO;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;
import com.stylefeng.guns.rest.vo.cinema.FilmInfoVO;
import com.stylefeng.guns.rest.vo.cinema.HallInfoVO;
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
