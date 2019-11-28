package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;
import com.stylefeng.guns.rest.vo.FilmsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {

    @Autowired
    MtimeFilmTMapper filmTMapper;

    @Override
    public String selectById(Integer id){
        MtimeFilmT mtimeFilmT = filmTMapper.selectById(id);
        return mtimeFilmT.getFilmName();
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
