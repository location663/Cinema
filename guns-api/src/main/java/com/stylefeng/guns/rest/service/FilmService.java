package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmForCinema;
import com.stylefeng.guns.rest.vo.FilmInfoForCinema;
import com.stylefeng.guns.rest.vo.FilmRequestVO;

public interface FilmService {
    String selectById(Integer id);

    BaseResponVO listFilms(FilmRequestVO filmRequestVO);

    FilmForCinema getFilmByFilmId(Integer filmId);

    FilmInfoForCinema getFilmInfoByFilmId(Integer filmId);
}
