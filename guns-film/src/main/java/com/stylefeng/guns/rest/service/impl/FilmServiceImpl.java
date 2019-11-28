package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.util.Mtime2VoTrans;
import com.stylefeng.guns.rest.vo.film.*;
import com.stylefeng.guns.rest.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {
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
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);

        //这是干啥的，还缺一个分页
//        if(isLimit){
//            filmInfos.subList(0,)
//        }
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

        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(filmTEntityWrapper);
        List<FilmInfo> filmInfos = Mtime2VoTrans.filmTrans(mtimeFilmTS);
        //这是干啥的，还缺一个分页
//        if(isLimit){
//            filmInfos.subList(0,)
//        }
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
        Page<MtimeFilmT> page=new Page<>(1,10,"film_box_office");
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
}
