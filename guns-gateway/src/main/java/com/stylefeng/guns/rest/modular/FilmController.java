package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.common.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;
import com.stylefeng.guns.rest.vo.film.FilmConditionVo;
import com.stylefeng.guns.rest.vo.film.FilmIndexVo;
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

    @Reference(interfaceClass = FilmService.class, check = false)
    private FilmService filmService;

    /**
     * 查询首页
     * @param islimit
     * @param page
     * @return
     */
    @RequestMapping("getIndex")
    public BaseResponVO getIndex(boolean islimit,int page){
        FilmIndexVo indexVo = new FilmIndexVo();
        indexVo.setBanners(filmService.getBanners());
        indexVo.setBoxRanking(filmService.getBoxRanking());
        indexVo.setExpectRanking(filmService.getExpectRanking());
        indexVo.setHotFilms(filmService.getHotFilms(islimit,page));
        indexVo.setSoonFilms(filmService.getSoonFilms(islimit,page));
        indexVo.setTop100(filmService.getTop());
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(indexVo);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setStatus(0);
        return baseResponVO;
    }
    @RequestMapping("getConditionList")
    public BaseResponVO getConditionList(){
        FilmConditionVo conditionVo = new FilmConditionVo();
        conditionVo.setCatInfo(filmService.getCats());
        conditionVo.setSourceInfo(filmService.getSources());
        conditionVo.setYearInfo(filmService.getYears());
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(conditionVo);
        baseResponVO.setStatus(0);
        return baseResponVO;
    }
    /**
     * 影片查询接口
     * @param filmRequestVO
     * @param bindingResult
     * @return
     */
    @RequestMapping("/getFilms")
    public BaseResponVO getFilms(@Valid FilmRequestVO filmRequestVO, BindingResult bindingResult) throws CinemaParameterException {
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
    public BaseResponVO filmDetailInfo(@PathVariable("filmInfo") String filmInfo, Integer searchType){

//        filmService.filmDetailInfo
        return null;
    }

}

