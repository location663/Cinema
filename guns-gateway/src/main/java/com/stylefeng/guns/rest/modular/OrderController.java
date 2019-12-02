package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {


    @Reference(interfaceClass = OrderService.class, check = false)
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

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
