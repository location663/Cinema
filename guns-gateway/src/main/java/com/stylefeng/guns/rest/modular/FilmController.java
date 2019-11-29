package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.common.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @Reference(interfaceClass = FilmService.class, check=false)
    private FilmService filmService;


    /**
     * 影片查询接口
     * @param filmRequestVO
     * @param bindingResult
     * @return
     */
    @RequestMapping("/getFilms")
    public BaseResponVO getFilms(@Valid FilmRequestVO filmRequestVO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
//            FieldError fieldError = bindingResult.getFieldError();
//            String field = fieldError.getField();
//            String defaultMessage = fieldError.getDefaultMessage();
//            System.out.println(field);
//            System.out.println(defaultMessage);
//            BaseResponVO baseResponVO = new BaseResponVO();
//            baseResponVO.setMsg(CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
//            baseResponVO.setStatus();

            throw new CinemaParameterException();

        }
        return  filmService.listFilms(filmRequestVO);
    }

    @RequestMapping("/films/{filmInfo}")
    public BaseResponVO filmDetailInfo(@PathVariable("filmInfo") String filmInfo, Integer searchType) throws Exception {
        BaseResponVO baseResponVO = null;
        if (0 == searchType && filmInfo.matches("^[1-9]\\d*$")){
            baseResponVO = filmService.getFilmById(Integer.parseInt(filmInfo));
        } else if (1 == searchType) {
            baseResponVO = filmService.getFilmByName(filmInfo);
        } else {
            throw new CinemaParameterException();
        }
        return baseResponVO;
    }

}

