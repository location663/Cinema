package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.PayService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {

    @Reference(interfaceClass = PayService.class, check = false)
    private PayService payService;

    @RequestMapping("order/getPayInfo")
    public BaseResponVO getPayInfo(Integer orderId) throws CinemaParameterException {
        BaseResponVO baseResponVO = payService.getPayInfo(orderId);
        return baseResponVO;
    }

    @RequestMapping("order/getPayResult")
    public BaseResponVO getPayResult(Integer orderId, Integer tryNums) throws CinemaParameterException {
        return payService.getPayResult(orderId);
    }
}
