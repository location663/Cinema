package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.util.Mtime2VoTrans;
import com.stylefeng.guns.rest.vo.film.*;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmInfoT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmsVO;
import com.stylefeng.guns.rest.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {
    @Autowired
    MtimeFilmInfoTMapper FilmInfoTMapper;

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


    @Override
    public List<BannerVo> getBanners() {
        List<MtimeBannerT> mtimeBannerS = bannerTMapper.selectList(null);
        List<BannerVo> list=new ArrayList<BannerVo>();
        for (MtimeBannerT mtimeBanner : mtimeBannerS) {
            BannerVo bannerVo = new BannerVo();
            bannerVo.setBannerAddress(mtimeBanner.getBannerAddress());
            bannerVo.setBannerId(mtimeBanner.getUuid());
            bannerVo.setBannerUrl(mtimeBanner.getBannerUrl());
            list.add(bannerVo);
        }
        return list;
    }
    //查询热映

    @Override
    public FilmVo getHotFilms(boolean isLimit, int page) {
        //查询filminfo
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",1);
        List<MtimeFilmT> mtimeFilmTS;
        if(isLimit){
            Page page1=new Page(1,page);
            mtimeFilmTS= filmTMapper.selectPage(page1,filmTEntityWrapper);
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
    public FilmVo getSoonFilms(boolean isLimit, int page) {
        //查询filminfo
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",2);
        List<MtimeFilmT> mtimeFilmTS;
        if(isLimit){
            Page page1=new Page(1,page);
            mtimeFilmTS = filmTMapper.selectPage(page1, filmTEntityWrapper);
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
        Page page=new Page(1,10,"film_box_office");
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",2);
        Page<MtimeFilmT> page=new Page<>(1,10,"film_preSaleNum");
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        filmTEntityWrapper.eq("film_status",3);
        Page<MtimeFilmT> page=new Page<>(1,10,"film_score");
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
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

    /**
     * 根据filmId查询film对象返回给Cinema
     * @param filmId
     * @return
     */
    @Override
    public FilmForCinema getFilmByFilmId(Integer filmId) {
        MtimeFilmT mtimeFilmT = filmTMapper.selectById(filmId);
        FilmForCinema filmForCinema = new FilmForCinema();
        BeanUtils.copyProperties(mtimeFilmT,filmForCinema);
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
        List<MtimeFilmInfoT> mtimeFilmInfoTS = FilmInfoTMapper.selectList(mtimeFilmInfoTEntityWrapper);

        FilmInfoForCinema filmInfoForCinema = new FilmInfoForCinema();
        BeanUtils.copyProperties(mtimeFilmInfoTS.get(0),filmInfoForCinema);
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


}
