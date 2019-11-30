package com.stylefeng.guns.rest.modular;
import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.FieldInfo;
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
     * @param brandId  影院编号
     * @param hallType 影厅类型
     * @param areaId 行政区编号
     * @param pageSize 每页条数
     * @param nowPage 当前页数
     * @return
     */
    @RequestMapping("/getCinemas")
    public BaseResponVO getCinemas(Integer brandId, Integer hallType, Integer areaId,Integer pageSize,Integer nowPage){
        BaseResponVO baseResponVO = cinemaService.getCinemasList(brandId,hallType,areaId,pageSize,nowPage);
        return baseResponVO;
    }

    /**
     *
     * @param brandId  影院编号
     * @param hallType 影厅类型
     * @param areaId   行政区编号
     * @return
     */
    @RequestMapping("/getCondition")
    public BaseResponVO getCondition(Integer brandId,Integer hallType,Integer areaId){
        BaseResponVO baseResponVO = cinemaService.getConditionList(brandId,hallType,areaId);
        return baseResponVO;
    }

}

