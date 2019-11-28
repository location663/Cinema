package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;

public interface FilmService {
    String selectById(Integer id);

    BaseResponVO listFilms(FilmRequestVO filmRequestVO);
}
