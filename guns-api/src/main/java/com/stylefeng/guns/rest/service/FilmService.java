package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;

public interface FilmService {

    BaseResponVO listFilms(FilmRequestVO filmRequestVO) throws Exception;

    BaseResponVO getFilmById(int id) throws Exception;

    BaseResponVO getFilmByName(String name) throws Exception;
}
