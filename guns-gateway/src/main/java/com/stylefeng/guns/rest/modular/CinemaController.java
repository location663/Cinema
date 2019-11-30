package com.stylefeng.guns.rest.modular;

import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

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

    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;

    @RequestMapping("/getCinemas")
    public BaseResponVO getCinemas(Integer brandId, Integer hallType, Integer districtId,Integer pageSize,Integer nowPage){
        BaseResponVO baseResponVO = cinemaService.getCinemasList(brandId,hallType,districtId,pageSize,nowPage);
        return baseResponVO;
    }

    @RequestMapping("/getCondition")
    public BaseResponVO getCondition(Integer brandId,Integer hallType,Integer areaId){
        BaseResponVO baseResponVO = cinemaService.getConditionList(brandId,hallType,areaId);
        return baseResponVO;
    }

}

