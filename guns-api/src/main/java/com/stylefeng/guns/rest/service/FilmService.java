package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;

public interface FilmService {
    String selectById(Integer id);

    BaseResponVO listFilms(Integer showType, Integer sortId, Integer catId, Integer sourceId, Integer yearId, Integer nowPage, Integer pageSize);
}
