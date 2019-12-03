package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaGetCinemasVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaPartVO;
import com.stylefeng.guns.rest.vo.promo.PromoInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public BaseResponVO getPromo(CinemaGetCinemasVO cinemaGetCinemasVO) throws CinemaParameterException {

        List<CinemaPartVO> partOfCinemasValue = cinemaService.getPartOfCinemasValue(cinemaGetCinemasVO);
        if (partOfCinemasValue.isEmpty()){
            return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
        }
        ArrayList<PromoInfoVO> promoInfoVOS = new ArrayList<>();
        for (CinemaPartVO cinemaPartVO : partOfCinemasValue) {
            promoInfoVOS.add(getPromoInfo(cinemaPartVO));
        }
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(promoInfoVOS);
        baseResponVO.setStatus(0);
        return baseResponVO;
    }

    private PromoInfoVO getPromoInfo(CinemaPartVO cinemaPartVO) throws CinemaParameterException {
        PromoInfoVO promoInfoVO = new PromoInfoVO();
        EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
        promoEntityWrapper.eq("cinemaId", cinemaPartVO.getCinemaId());
        List<MtimePromo> mtimePromos = promoMapper.selectList(promoEntityWrapper);

        if (mtimePromos.isEmpty()){
            return null;
        }
        MtimePromo mtimePromo = mtimePromos.get(0);
        BeanUtils.copyProperties(cinemaPartVO, promoInfoVO);
        BigDecimal price = mtimePromo.getPrice();
        promoInfoVO.setPrice(price.doubleValue());
        promoInfoVO.setDescription(mtimePromo.getDescription());
        promoInfoVO.setStartTime(TransferUtils.parseDate2String(mtimePromo.getStartTime()));
        promoInfoVO.setEndTime(TransferUtils.parseDate2String(mtimePromo.getEndTime()));
        promoInfoVO.setStatus(mtimePromo.getStatus());
        EntityWrapper<MtimePromoStock> promoStockEntityWrapper = new EntityWrapper<>();
        promoStockEntityWrapper.eq("promoId", mtimePromo.getUuid());
        List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(promoStockEntityWrapper);
        if (mtimePromoStocks.isEmpty()) {
            throw new CinemaParameterException();
        }
        MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
        promoInfoVO.setStock(mtimePromoStock.getStock());
        promoInfoVO.setUuid(mtimePromo.getUuid());
        return promoInfoVO;
    }


}
