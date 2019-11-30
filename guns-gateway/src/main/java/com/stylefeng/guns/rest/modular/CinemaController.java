package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetConditionVO;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 影院信息表 前端控制器
 * </p>
 *
 * @author ali
 * @since 2019-11-27
 */
@RestController
@RequestMapping("cinema")
public class CinemaController {

    @Reference(interfaceClass = CinemaService.class, check = false)
    CinemaService cinemaService;

    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @RequestMapping("getFieldInfo")
    public BaseResponVO getFieldInfo(Integer cinemaId, Integer fieldId){
        FieldInfo fieldInfo = cinemaService.getFieldInfo(cinemaId, fieldId);
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(fieldInfo);
        baseResponVO.setImgPre("http://img.meetingshop.cn");
        baseResponVO.setStatus(0);
        return baseResponVO;
    }

    /**
     * 获取播放场次接口
     * @param cinemaId
     * @return
     */
    @RequestMapping("getFields")
    public BaseResponVO getFields(Integer cinemaId) throws Exception {
        FieldInfo fieldInfo = cinemaService.getFields(cinemaId);
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(fieldInfo);
        baseResponVO.setImgPre("http://img.meetingshop.cn");
        baseResponVO.setStatus(0);
        return baseResponVO;
    }

    /**
     *
     * @param cinemaGetCinemasVO
     * @return
     */
    @RequestMapping("/getCinemas")
    public BaseResponVO getCinemas(@Valid CinemaGetCinemasVO cinemaGetCinemasVO){
        BaseResponVO baseResponVO = cinemaService.getCinemasList(cinemaGetCinemasVO);
        return baseResponVO;
    }

    /**
     *
     * @param cinemaGetConditionVO
     * @return
     */
    @RequestMapping("/getCondition")
    public BaseResponVO getCondition(@Valid CinemaGetConditionVO cinemaGetConditionVO){
        BaseResponVO baseResponVO = cinemaService.getConditionList(cinemaGetConditionVO.getBrandId(),cinemaGetConditionVO.getHallType(),cinemaGetConditionVO.getAreaId());
        return baseResponVO;
    }

}

