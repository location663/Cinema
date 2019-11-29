package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.*;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {

    @Autowired
    MtimeFilmTMapper filmTMapper;

    @Autowired
    MtimeFilmInfoTMapper filmInfoTMapper;

    @Autowired
    MtimeCatDictTMapper catDictTMapper;

    @Autowired
    MtimeSourceDictTMapper sourceDictTMapper;

    @Autowired
    MtimeFilmActorTMapper filmActorTMapper;

    @Autowired
    MtimeActorTMapper actorTMapper;

    @Override
    public BaseResponVO listFilms(FilmRequestVO filmRequestVO) {
        BaseResponVO baseResponVO = new BaseResponVO();
        EntityWrapper<MtimeFilmT> mtimeFilmTEntityWrapper = new EntityWrapper<>();
        mtimeFilmTEntityWrapper.eq("film_status", filmRequestVO.getShowType());
        if (99 != filmRequestVO.getCatId()) {
            mtimeFilmTEntityWrapper.like("film_cats", "#" + filmRequestVO.getCatId() + "#");
        }
        if (99 != filmRequestVO.getYearId()) {
            mtimeFilmTEntityWrapper.eq("film_date", filmRequestVO.getYearId());
        }
        if (99 != filmRequestVO.getSourceId()) {
            mtimeFilmTEntityWrapper.eq("film_area", filmRequestVO.getSourceId());
        }
        if (1 == filmRequestVO.getSortId()){
            mtimeFilmTEntityWrapper.orderBy(false, "film_status");
        } else if (2 == filmRequestVO.getSortId()){
            mtimeFilmTEntityWrapper.orderBy(false, "film_date");
        } else if (3 == filmRequestVO.getSortId()) {
            mtimeFilmTEntityWrapper.orderBy(false, "film_score");
        }
        Page<MtimeFilmT> mtimeFilmTPage = new Page(filmRequestVO.getNowPage(), filmRequestVO.getPageSize());
        List<Map<String, Object>> maps = filmTMapper.selectMapsPage(mtimeFilmTPage, mtimeFilmTEntityWrapper);
        List<FilmsVO> res = trans2Films(maps);
        baseResponVO.setStatus(0);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setNowPage(filmRequestVO.getNowPage() + 1 );
        baseResponVO.setTotalPage((int) Math.ceil(1.0*maps.size()/filmRequestVO.getPageSize()));
        baseResponVO.setData(res);
        return baseResponVO;
    }

    @Override
    public BaseResponVO getFilmById(int id) throws Exception {
        BaseResponVO baseResponVO = new BaseResponVO();
        FilmDetailVO filmDetailVO = new FilmDetailVO();
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("UUID", id);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        if (Collections.isEmpty(mtimeFilmTS)){
            throw new CinemaException(1, "查询失败，无影片可加载");
        }
        MtimeFilmT filmT = mtimeFilmTS.get(0);
        EntityWrapper<MtimeFilmInfoT> filmInfoTEntityWrapper = new EntityWrapper<>();
        filmInfoTEntityWrapper.eq("film_id", id);
        List<MtimeFilmInfoT> filmInfoTS = filmInfoTMapper.selectList(filmInfoTEntityWrapper);
        MtimeFilmInfoT filmInfoT = filmInfoTS.get(0);
        filmDetailVO.setFilmName(filmT.getFilmName());
        filmDetailVO.setFilmEnName(filmInfoT.getFilmEnName());
        filmDetailVO.setImgAddress(filmT.getImgAddress());
        filmDetailVO.setScore(filmT.getFilmScore());
        Integer filmScoreNum = filmInfoT.getFilmScoreNum();
        filmDetailVO.setScoreNum(filmScoreNum.toString());
        Integer filmBoxOffice = filmT.getFilmBoxOffice();
        filmDetailVO.setTotalBox(filmBoxOffice.toString());
        String filmCats = filmT.getFilmCats();
        String[] cats = filmCats.split("#");
        StringBuilder sb = new StringBuilder();
        for (String cat : cats) {
            if (!"".equals(cat.trim())) {
                MtimeCatDictT mtimeCatDictT = catDictTMapper.selectById(cat);
                sb.append(mtimeCatDictT.getShowName() + "，");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        filmDetailVO.setInfo01(sb.toString());
        MtimeSourceDictT mtimeSourceDictT = sourceDictTMapper.selectById(filmT.getFilmSource());
        filmDetailVO.setInfo02(mtimeSourceDictT.getShowName() + " / " + filmInfoT.getFilmLength() + "分钟");
        String s = parseDate2String(filmT.getFilmTime());
        filmDetailVO.setInfo03(s + " " +  mtimeSourceDictT.getShowName() + " 上映");

        InfoRequestVO infoRequestVO = new InfoRequestVO();
        infoRequestVO.setBiography(filmInfoT.getBiography());
        ActorsVO actorsVO = new ActorsVO();
        MtimeActorT directorDO = actorTMapper.selectById(filmInfoT.getDirectorId());
        DirectorVO directorVO = new DirectorVO();
        directorVO.setDirectorName(directorDO.getActorName());
        directorVO.setImgAddress(directorDO.getActorImg());
        EntityWrapper<MtimeFilmActorT> filmActorTEntityWrapper = new EntityWrapper<>();
        filmActorTEntityWrapper.eq("film_id", id);
        List<MtimeFilmActorT> mtimeFilmActorTS = filmActorTMapper.selectList(filmActorTEntityWrapper);
        ArrayList<ActorVO> actorVOS = new ArrayList<>();
        for (MtimeFilmActorT mtimeFilmActorT : mtimeFilmActorTS) {
            ActorVO actorVO = new ActorVO();
            MtimeActorT mtimeActorT = actorTMapper.selectById(mtimeFilmActorT.getActorId());
            actorVO.setDirectorName(mtimeActorT.getActorName());
            actorVO.setImgAddress(mtimeActorT.getActorImg());
            actorVO.setRoleName(mtimeFilmActorT.getRoleName());
            actorVOS.add(actorVO);
        }
        actorsVO.setDirector(directorVO);
        actorsVO.setActors(actorVOS);
        infoRequestVO.setActors(actorsVO);
        filmDetailVO.setFilmId(filmT.getUuid().toString());
        ImgVO imgVO = parseString2ImgVO(filmInfoT.getFilmImgs());
        infoRequestVO.setImgVO(imgVO);
        filmDetailVO.setInfo04(infoRequestVO);
        baseResponVO.setStatus(0);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setData(filmDetailVO);
        return baseResponVO;
    }

    @Override
    public BaseResponVO getFilmByName(String name) throws Exception {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_name", name);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        if (Collections.isEmpty(mtimeFilmTS)){
            throw new CinemaException(1, "查询失败，无影片可加载");
        }
        MtimeFilmT filmT = mtimeFilmTS.get(0);
        BaseResponVO filmById = getFilmById(filmT.getUuid());
        return filmById;
    }


    private List<FilmsVO> trans2Films(List<Map<String, Object>> maps) {
        ArrayList<FilmsVO> filmsVOS = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            FilmsVO filmsVO = new FilmsVO();
            Object uuid = map.get("uuid");
            filmsVO.setFilmId(uuid.toString());
            filmsVO.setFilmType((Integer) map.get("filmType"));
            filmsVO.setImgAddress((String) map.get("imgAddress"));
            filmsVO.setFilmName((String) map.get("filmName"));
            filmsVO.setFilmScore((String) map.get("filmScore"));
            filmsVOS.add(filmsVO);
        }
        return filmsVOS;
    }

    private ImgVO parseString2ImgVO(String imgString){
        String[] imgs = imgString.split(",");
        ImgVO imgVO = new ImgVO();
        if (imgs.length >= 5) {
            imgVO.setMainImg(imgs[0]);
            imgVO.setImg01(imgs[1]);
            imgVO.setImg02(imgs[2]);
            imgVO.setImg03(imgs[3]);
            imgVO.setImg04(imgs[4]);
        }
        return imgVO;
    }

    private String parseDate2String(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
