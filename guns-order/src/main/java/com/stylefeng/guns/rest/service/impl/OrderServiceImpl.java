package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.util.GetSeats;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaNameAndFilmIdVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import com.stylefeng.guns.rest.vo.order.SeatVO;
import com.stylefeng.guns.rest.vo.order.SeatsVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private String[] seatString = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 购票
     * @param buyTicketDTO
     * @return
     */
    @Override
    public BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO, UserVO userVO) {

        OrderInfoVO orderInfoVO = new OrderInfoVO();
        MoocOrderT moocOrderT = new MoocOrderT();
        CinemaNameAndFilmIdVO cinemaNameAndFilmIdByFieldId = cinemaService.getCinemaNameAndFilmIdByFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setCinemaId(cinemaNameAndFilmIdByFieldId.getCinemaId());
        moocOrderT.setFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setFilmId(cinemaNameAndFilmIdByFieldId.getFilmId());
        moocOrderT.setSeatsIds(buyTicketDTO.getSoldSeats());

        String soldSeats = buyTicketDTO.getSoldSeats();
        String[] seats = soldSeats.split(",");
//        StringBuilder sb = new StringBuilder();
//        for (String seat : seats) {
//            int i = Integer.parseInt(seat);
//            sb.append("第" + seatString[i / 10] + "排" + (i % 10) + "座,");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        moocOrderT.setSeatsName(sb.toString());

        SeatsVO seatsFromFront = GetSeats.getSeatsFromFront("http://localhost:1818/json/4dx.json");
        String[] seatIds = seatsFromFront.getIds().split(",");
        SeatVO[][] single = seatsFromFront.getSingle();
        SeatVO[][] couple = seatsFromFront.getCouple();

        StringBuilder sb = new StringBuilder();

        for (String seat : seats) {

            if (!seat.matches("^[1-9]\\d*$") || Integer.parseInt(seat) > Integer.parseInt(seatIds[seatIds.length-1])){
                return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
            }
            int seatId = Integer.parseInt(seat);
//            int temp = seatId;
//            for (int i = 0; i < single.length; i++) {
//                if (temp > single[i].length){
//                    temp -= single[i].length;
//                } else {
//                    for (int j = 0; j < single[i].length; j++) {
//                        if (single[i][j].getSeatId() == seatId){
//                            sb.append("第" + seatString[single[i][j].getRow()] + "排" + single[i][j].getColumn() + "座,");
//                            break;
//                        }
//                    }
//                }
//            }
//
//            for (int i = 0; i < couple.length; i++) {
//                if (temp > couple[i].length){
//                    temp -= couple[i].length;
//                } else {
//                    for (int j = 0; j < couple[i].length; j++) {
//                        if (couple[i][j].getSeatId() == seatId){
//                            sb.append("第" + seatString[couple[i][j].getRow()] + "排" + couple[i][j].getColumn() + "座,");
//                            break;
//                        }
//                    }
//                }
//            }
            String detailSeat = getDetailSeat(single, seatId);
            if (null != detailSeat){
                sb.append(detailSeat);
            } else if (null != getDetailSeat(couple, seatId)){
                sb.append(getDetailSeat(couple, seatId));
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        moocOrderT.setSeatsName(sb.toString());

        Integer price = cinemaNameAndFilmIdByFieldId.getPrice();
        moocOrderT.setFilmPrice(1.0 * price);
        moocOrderT.setOrderPrice(1.0 * price * seats.length);

        moocOrderT.setOrderTime(new Date());

        moocOrderT.setOrderUser(userVO.getUuid());
        moocOrderT.setOrderStatus(0);
        Integer insert = orderTMapper.insert(moocOrderT);
        if (0 == insert) {
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

    @Override
    public BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO) {
        Page<MoocOrderT> objectPage = new Page<>(nowPage,pageSize);
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("order_user",userVO.getUuid());
        List<Map<String, Object>> maps = orderTMapper.selectMapsPage(objectPage, entityWrapper);
        List<OrderInfoVO> list = orderDO2OrderInfo(maps);
        return new BaseResponVO(0,null, list,null,null,"");
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> orderTEntityWrapper = new EntityWrapper<>();
        orderTEntityWrapper.eq("field_id", fieldId);
        List<MoocOrderT> moocOrderTS = orderTMapper.selectList(orderTEntityWrapper);
        StringBuilder sb = new StringBuilder();
        for (MoocOrderT moocOrderT : moocOrderTS) {
            sb.append(moocOrderT.getSeatsIds() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private List<OrderInfoVO> orderDO2OrderInfo(List<Map<String, Object>> maps) {
        ArrayList<OrderInfoVO> orderInfoVOS = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            OrderInfoVO orderInfoVO = new OrderInfoVO();
            CinemaNameAndFilmIdVO fieldInfo = cinemaService.getCinemaNameAndFilmIdByFieldId((Integer) map.get("field_id"));
            orderInfoVO.setCinemaName(fieldInfo.getCinemaName());

            Integer uuid = (Integer) map.get("UUID");
            orderInfoVO.setOrderId(uuid.toString());
            Double order_price = (Double) map.get("order_price");
            orderInfoVO.setOrderPrice(order_price.toString());
            Integer status = (Integer) map.get("orderStatus");
            orderInfoVO.setOrderStatus(orderStatus[status]);
            Date order_time = (Date) map.get("order_time");
            Long time = order_time.getTime();
            orderInfoVO.setOrderTimestamp(time.toString());
            String seats_name = (String) map.get("seats_ids");
            orderInfoVO.setSeatsName(seats_name);
            orderInfoVOS.add(orderInfoVO);
        }
        return orderInfoVOS;
    }

    private String getDetailSeat(SeatVO[][] seats, int seatId){
        int temp = seatId - seats[0][0].getSeatId() + 1;
        String res = null;
        for (int i = 0; i < seats.length; i++) {
            if (temp > seats[i].length){
                temp -= seats[i].length;
            } else {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getSeatId() == seatId){
                        res = "第" + seatString[seats[i][j].getRow()] + "排" + seats[i][j].getColumn() + "座,";
                        return res;
                    }
                }
            }
        }
        return res;
    }
}
