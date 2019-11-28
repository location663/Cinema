package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 影片主表 前端控制器
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
@RestController
@RequestMapping("film")
public class FilmController {

    @Reference(interfaceClass = FilmService.class)
    FilmService filmService;

    @RequestMapping("query")
    public String test(Integer id){
        String s = filmService.selectById(id);
        return s;
    }

    @RequestMapping("/getFilms")
    public BaseResponVO getFilms(Integer showType, Integer sortId, Integer catId
            , Integer sourceId, Integer yearId, Integer nowPage, Integer pageSize){
        BaseResponVO baseResponVO = filmService.listFilms(showType, sortId, catId
                , sourceId, yearId, nowPage, pageSize);
        return baseResponVO;
    }

}

