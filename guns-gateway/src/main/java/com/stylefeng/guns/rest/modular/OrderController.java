package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {


    @Reference(interfaceClass = OrderService.class, check = false)
    private OrderService orderService;

    private RedisTemplate redisTemplate;

    @RequestMapping("buyTickets")
    public BaseResponVO buyTickets(BuyTicketDTO buyTicketDTO){
        BaseResponVO baseResponVO = orderService.insertOrder(buyTicketDTO);
        return baseResponVO;
    }




    /**
     * 获取用户订单信息
     * @param nowPage
     * @param pageSize
     * @return
     */
    @RequestMapping("getOrderInfo")
    public BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize) throws Exception {
        if (nowPage == null || pageSize == null || nowPage <=0 || pageSize <= 0){
            throw new Exception();
        }
        String token = HttpKit.getRequest().getHeader("Authorization").substring(7);
        UserVO userVO = (UserVO) redisTemplate.opsForValue().get(token);
        BaseResponVO baseResponVO = orderService.getOrderInfo(nowPage,pageSize,userVO);
        return baseResponVO;
    }


}
