package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaNameAndFilmIdVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper orderTMapper;

    @Reference(interfaceClass = CinemaService.class, check = false)
    private CinemaService cinemaService;

    @Reference(interfaceClass = FilmService.class, check = false)
    private FilmService filmService;

    @Autowired
    RedisTemplate redisTemplate;

    private String[] orderStatus = {"待支付", "已支付", "已关闭"};

    /**
     * 购票
     * @param buyTicketDTO
     * @return
     */
    @Override
    public BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO) {
        String[] seatString = {"一", "二", "三", "四", "五"};
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        MoocOrderT moocOrderT = new MoocOrderT();
        CinemaNameAndFilmIdVO cinemaNameAndFilmIdByFieldId = cinemaService.getCinemaNameAndFilmIdByFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setCinemaId(cinemaNameAndFilmIdByFieldId.getCinemaId());
        moocOrderT.setFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setFilmId(cinemaNameAndFilmIdByFieldId.getFilmId());
        moocOrderT.setSeatsIds(buyTicketDTO.getSoldSeats());

        String soldSeats = buyTicketDTO.getSoldSeats();
        String[] seats = soldSeats.split(",");
        StringBuilder sb = new StringBuilder();
        for (String seat : seats) {
            int i = Integer.parseInt(seat);
            sb.append("第" + seatString[i/10] + "排" + (i%10) + "座,");
        }
        sb.deleteCharAt(sb.length()-1);
        moocOrderT.setSeatsName(sb.toString());

        Integer price = cinemaNameAndFilmIdByFieldId.getPrice();
        moocOrderT.setFilmPrice(1.0 * price);
        moocOrderT.setOrderPrice(1.0 * price * seats.length);

        moocOrderT.setOrderTime(new Date());

        String authorization = HttpKit.getRequest().getHeader("Authorization");
        String token = authorization.substring(7);
        UserVO userVO = (UserVO) redisTemplate.opsForValue().get(token);
        moocOrderT.setOrderUser(userVO.getUuid());
        moocOrderT.setOrderUser(0);
        Integer insert = orderTMapper.insert(moocOrderT);
        if (0 == insert){
            return new ErrorResponVO(CinemaExceptionEnum.BUYTICKETS_ERROR.getStatus(), CinemaExceptionEnum.BUYTICKETS_ERROR.getMsg());
        }
        Integer id = orderTMapper.selectLastInsertId();
        orderInfoVO.setCinemaName(cinemaNameAndFilmIdByFieldId.getCinemaName());
        orderInfoVO.setFieldTime(TransferUtils.parseDate2String(moocOrderT.getOrderTime()));
        orderInfoVO.setFilmName(filmService.getFilmByFilmId(cinemaNameAndFilmIdByFieldId.getFilmId()).getFilmName());
        orderInfoVO.setOrderId(id.toString());
        orderInfoVO.setOrderPrice(moocOrderT.getOrderPrice().toString());
        orderInfoVO.setOrderStatus(orderStatus[moocOrderT.getOrderStatus()]);
        Long time = moocOrderT.getOrderTime().getTime();
        orderInfoVO.setOrderTimestamp(time.toString());
        orderInfoVO.setSeatsName(buyTicketDTO.getSeatsName());
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(orderInfoVO);
        baseResponVO.setStatus(0);
        return baseResponVO;
    }
}
