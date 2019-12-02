package com.alipay.demo.trade.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.service.PayService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService {

    @Override
    public BaseResponVO getPayInfo(Integer orderId) {

        return null;
    }
}
