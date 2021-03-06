package com.stylefeng.guns.rest.modular;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.common.CacheService;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.FilmRequestVO;
import com.stylefeng.guns.rest.vo.film.FilmConditionVo;
import com.stylefeng.guns.rest.vo.film.FilmIndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate redisTemplate;

    private final String GET_FILMS = "GET_FILMS";

    @Autowired
    private CacheService cacheService;

    /**
     * 查询首页
     * @return
     */
    @RequestMapping("getIndex")
    public BaseResponVO getIndex(){
        FilmIndexVo indexVo = new FilmIndexVo();
        indexVo.setBanners(filmService.getBanners());
        indexVo.setBoxRanking(filmService.getBoxRanking());
        indexVo.setExpectRanking(filmService.getExpectRanking());
        indexVo.setHotFilms(filmService.getHotFilms(true,10));
        indexVo.setSoonFilms(filmService.getSoonFilms(true,10));
        indexVo.setTop100(filmService.getTop());
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(indexVo);
        baseResponVO.setImgPre("http://img.meetingshop.cn/");
        baseResponVO.setStatus(0);
        return baseResponVO;
    }



    @RequestMapping("getConditionList")
    public BaseResponVO getConditionList(@RequestParam(value = "catId", required = true) Integer catId,
                                         @RequestParam(value = "sourceId", required = true) Integer sourceId,
                                         @RequestParam(value = "yearId", required = true) Integer yearId){
        FilmConditionVo conditionVo = new FilmConditionVo();
        conditionVo.setCatInfo(filmService.getCats(catId));
        conditionVo.setSourceInfo(filmService.getSources(sourceId));
        conditionVo.setYearInfo(filmService.getYears(yearId));
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

//        Object o1 = cacheService.get(GET_FILMS);
//        if (null != o1){
//            return (BaseResponVO) o1;
//        }


        Object o = redisTemplate.opsForValue().get(GET_FILMS);
        if (null != o){
//            cacheService.put(GET_FILMS, (BaseResponVO)o);
            return (BaseResponVO) o;
        }
        BaseResponVO baseResponVO = filmService.listFilms(filmRequestVO);
        redisTemplate.opsForValue().set(GET_FILMS, baseResponVO);
        redisTemplate.expire(GET_FILMS, 60, TimeUnit.MINUTES);
//        cacheService.put(GET_FILMS, (BaseResponVO)o);

        return baseResponVO;
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

