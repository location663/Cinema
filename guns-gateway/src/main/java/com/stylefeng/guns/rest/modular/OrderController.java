package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    @Reference(interfaceClass = OrderService.class, check = false)
    private OrderService orderService;

    @RequestMapping("buyTickets")
    public BaseResponVO buyTickets(BuyTicketDTO buyTicketDTO){
        BaseResponVO baseResponVO = orderService.insertOrder(buyTicketDTO);
        return baseResponVO;
    }
}
