package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetConditionVO;
import com.stylefeng.guns.rest.vo.promo.PromoInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = PromoService.class)
public class PromoServiceImpl implements PromoService {

    @Reference(interfaceClass = CinemaService.class, check = false)
    private CinemaService cinemaService;

    @Autowired
    MtimePromoMapper promoMapper;

    @Autowired
    MtimePromoStockMapper promoStockMapper;

    @Autowired
    MtimePromoOrderMapper promoOrderMapper;

    @Override
    public BaseResponVO getPromo(CinemaGetConditionVO cinemaGetConditionVO) {


        EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
        promoEntityWrapper.eq("cinemaId", );
        promoMapper.


        return null;
    }


}
