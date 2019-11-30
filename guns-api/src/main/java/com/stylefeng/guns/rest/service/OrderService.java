package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.vo.BaseResponVO;

public interface OrderService {
    BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO);
}
