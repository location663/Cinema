package com.stylefeng.guns.rest.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;

import com.stylefeng.guns.rest.common.utils.TransferUtils;

import com.stylefeng.guns.rest.exception.CinemaBusinessException;

import com.stylefeng.guns.rest.util.Mtime2VoTrans;
import com.stylefeng.guns.rest.vo.film.*;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmInfoT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;
import com.stylefeng.guns.rest.vo.FilmsVO;
import com.stylefeng.guns.rest.vo.*;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {
    @Autowired
    MtimeFilmInfoTMapper filmInfoTMapper;

    @Autowired
    MtimeFilmTMapper filmTMapper;

    @Autowired
    MtimeBannerTMapper bannerTMapper;

    @Autowired
    MtimeCatDictTMapper catDictTMapper;

    @Autowired
    MtimeYearDictTMapper yearDictTMapper;

    @Autowired
    MtimeSourceDictTMapper sourceDictTMapper;

    @Autowired
    MtimeFilmActorTMapper filmActorTMapper;

    @Autowired
    MtimeActorTMapper actorTMapper;

    @Override
    public List<BannerVo> getBanners(){
        List<MtimeBannerT> mtimeBannerS = bannerTMapper.selectList(null);
        List<BannerVo> list=new ArrayList<BannerVo>();
        for (MtimeBannerT mtimeBanner : mtimeBannerS) {
            BannerVo bannerVo = new BannerVo();
            bannerVo.setBannerAddress(mtimeBanner.getBannerAddress());
            bannerVo.setBannerId(mtimeBanner.getUuid());
            bannerVo.setBannerUrl(mtimeBanner.getBannerUrl());
            list.add(bannerVo);
        }
        if(Collections.isEmpty(list)){
            throw new CinemaBusinessException();
        }
        return list;
    }
    //查询热映

    @Override
    public FilmVo getHotFilms(boolean isLimit, int num) {
        //查询filminfo
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",1);
        List<MtimeFilmT> mtimeFilmTS;
        if(isLimit){
            Page page=new Page(1,num,"film_box_office",false);
            mtimeFilmTS= filmTMapper.selectPage(page,filmTEntityWrapper);
        }
        else {
            mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        }
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        int filmNum=mtimeFilmTS.size();
        FilmVo filmVo = new FilmVo();
        filmVo.setFilmInfo(filmInfos);
        filmVo.setFilmNum(filmNum);
        return filmVo;
    }

    @Override
    public FilmVo getSoonFilms(boolean isLimit, int num) {
        //查询filminfo
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",2);
        List<MtimeFilmT> mtimeFilmTS;
        if(isLimit){
            Page page=new Page(1,num,"film_preSaleNum",false);
            mtimeFilmTS = filmTMapper.selectPage(page, filmTEntityWrapper);
        }
        else{
            mtimeFilmTS= filmTMapper.selectList(filmTEntityWrapper);
        }
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        int filmNum=mtimeFilmTS.size();
        FilmVo filmVo = new FilmVo();
        filmVo.setFilmInfo(filmInfos);
        filmVo.setFilmNum(filmNum);
        return filmVo;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",1);
        Page page=new Page(1,10,"film_box_office",false);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectPage(page,filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",2);
        Page<MtimeFilmT> page=new Page<>(1,10,"film_preSaleNum",false);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectPage(page,filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",3);
        Page<MtimeFilmT> page=new Page<>(1,10,"film_score",false);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectPage(page,filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<CatVo> getCats() {
        List<CatVo> cats = new ArrayList<>();
        List<MtimeCatDictT> catDictTS = catDictTMapper.selectList(null);
        for (MtimeCatDictT catDictT : catDictTS) {
            CatVo catVo = new CatVo();
            catVo.setCatId(catDictT.getUuid());
            catVo.setCatName(catDictT.getShowName());
            cats.add(catVo);
        }
        return cats;
    }

    @Override
    public List<SourceVo> getSources() {
        List<SourceVo> sources = new ArrayList<>();
        List<MtimeSourceDictT> sourceDictTS = sourceDictTMapper.selectList(null);
        for (MtimeSourceDictT sourceDictT : sourceDictTS) {
            SourceVo sourceVo = new SourceVo();
            sourceVo.setSourceName(sourceDictT.getShowName());
            sourceVo.setSouceId(sourceDictT.getUuid()+"");
            sources.add(sourceVo);
        }
        return sources;
    }

    @Override
    public List<YearVo> getYears() {
       List<YearVo> years=new ArrayList<>();
        List<MtimeYearDictT> yearDictTS = yearDictTMapper.selectList(null);
        for (MtimeYearDictT yearDictT : yearDictTS) {
            YearVo yearVo = new YearVo();
            yearVo.setYearName(yearDictT.getShowName());
            yearVo.setYearId(yearDictT.getUuid()+"");
            years.add(yearVo);
        }
        return years;
    }

    /**
     * 根据传递的参数返回不同类型的电源列表
     * @param filmRequestVO
     * @return
     */
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
        if (null == filmRequestVO.getNowPage()){
            filmRequestVO.setNowPage(1);
        }
        if (null == filmRequestVO.getPageSize()){
            filmRequestVO.setPageSize(18);
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

    /**
     * 根据id返回电源的详细信息
     * @param id
     * @return
     * @throws Exception
     */
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
        String s = TransferUtils.parseDate2String(filmT.getFilmTime());
        filmDetailVO.setInfo03(s + " " +  mtimeSourceDictT.getShowName() + " 上映");

        InfoRequestVO infoRequestVO = new InfoRequestVO();
        infoRequestVO.setBiography(filmInfoT.getBiography());
        ActorsVO actorsVO = new ActorsVO();
        MtimeActorT directorDO = actorTMapper.selectById(filmInfoT.getDirectorId());
        DirectorVO directorVO = new DirectorVO();
        directorVO.setDirectorName(directorDO.getActorName());
        directorVO.setImgAddress(directorDO.getActorImg());
//        EntityWrapper<MtimeFilmActorT> filmActorTEntityWrapper = new EntityWrapper<>();
//        filmActorTEntityWrapper.eq("film_id", id);
//        List<MtimeFilmActorT> mtimeFilmActorTS = filmActorTMapper.selectList(filmActorTEntityWrapper);
//        ArrayList<ActorVO> actorVOS = new ArrayList<>();
//        for (MtimeFilmActorT mtimeFilmActorT : mtimeFilmActorTS) {
//            ActorVO actorVO = new ActorVO();
//            MtimeActorT mtimeActorT = actorTMapper.selectById(mtimeFilmActorT.getActorId());
//            actorVO.setDirectorName(mtimeActorT.getActorName());
//            actorVO.setImgAddress(mtimeActorT.getActorImg());
//            actorVO.setRoleName(mtimeFilmActorT.getRoleName());
//            actorVOS.add(actorVO);
//        }
        List<ActorVO> actorVOS1 = listActorVOByFilmId(id);
        actorsVO.setDirector(directorVO);
        actorsVO.setActors(actorVOS1);
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

    /**
     * 根据电源名字返回电源的详细信息
     * @param name
     * @return
     * @throws Exception
     */
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

    /**
     * 根据电影id返回参演人员的信息
     * @param filmId
     * @return
     */
    @Override
    public List<ActorVO> listActorVOByFilmId(Integer filmId) {
        EntityWrapper<MtimeFilmActorT> filmActorTEntityWrapper = new EntityWrapper<>();
        filmActorTEntityWrapper.eq("film_id", filmId);
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
        return actorVOS;
    }

    /**
     * 根据filmId查询film对象返回给Cinema
     * @param filmId
     * @return
     */
    @Override
    public FilmForCinema getFilmByFilmId(Integer filmId) {
        MtimeFilmT mtimeFilmT = filmTMapper.selectById(filmId);
        FilmForCinema filmForCinema = new FilmForCinema();
        if (null != mtimeFilmT) {
            BeanUtils.copyProperties(mtimeFilmT, filmForCinema);
        }
        return filmForCinema;
    }

    /**
     * 根据filmId查询film info对象返回给Cinema
     * @param filmId
     * @return
     */
    @Override
    public FilmInfoForCinema getFilmInfoByFilmId(Integer filmId) {
        EntityWrapper<MtimeFilmInfoT> mtimeFilmInfoTEntityWrapper = new EntityWrapper<>();
        mtimeFilmInfoTEntityWrapper.eq("film_id",filmId);
        List<MtimeFilmInfoT> mtimeFilmInfoTS = filmInfoTMapper.selectList(mtimeFilmInfoTEntityWrapper);

        FilmInfoForCinema filmInfoForCinema = new FilmInfoForCinema();
        if (!CollectionUtils.isEmpty(mtimeFilmInfoTS)){
            BeanUtils.copyProperties(mtimeFilmInfoTS.get(0),filmInfoForCinema);
        }
        return filmInfoForCinema;
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



}
