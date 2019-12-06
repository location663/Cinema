package com.stylefeng.guns.rest.service;
import com.stylefeng.guns.rest.vo.*;
import com.stylefeng.guns.rest.vo.film.*;

import java.util.List;

public interface FilmService {


    BaseResponVO listFilms(FilmRequestVO filmRequestVO) throws Exception;

    BaseResponVO getFilmById(int id) throws Exception;

    BaseResponVO getFilmByName(String name) throws Exception;

    List<BannerVo> getBanners();
    FilmVo getHotFilms(boolean isLimit,int page);
    FilmVo getSoonFilms(boolean isLimit,int page);
    List<FilmInfo> getBoxRanking();
    List<FilmInfo> getExpectRanking();
    List<FilmInfo> getTop();

    List<CatVo> getCats(Integer catId);

    List<SourceVo> getSources(Integer sourceId);

    List<YearVo> getYears(Integer yearId);

    FilmForCinema getFilmByFilmId(Integer filmId);

    FilmInfoForCinema getFilmInfoByFilmId(Integer filmId);

    List<ActorVO> listActorVOByFilmId(Integer filmId);

}
