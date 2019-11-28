package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.service.FilmService;
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


//    @RequestMapping("/getFilms")

}

