package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.service.UserService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    MtimeUserTMapper userTMapper;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public BaseResponVO userRegister(UserRegisterDTO user) {

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",user.getUsername());
        Integer count = userTMapper.selectCount(mtimeUserTEntityWrapper);
        if (count != 0){
            return new BaseResponVO(1,null,null,null,null,"用户已存在");
        }

        MtimeUserT mtimeUserT = new MtimeUserT();
        mtimeUserT.setUserName(user.getUsername());
        mtimeUserT.setUserPwd(user.getPassword());
        mtimeUserT.setEmail(user.getEmail());
        mtimeUserT.setUserPhone(user.getMobile());
        mtimeUserT.setAddress(user.getAddress());
        mtimeUserT.setBeginTime(new Date());
        mtimeUserT.setUpdateTime(new Date());

        Integer insert = userTMapper.insert(mtimeUserT);

        return new BaseResponVO(0,null,null,null,null,"注册成功");
    }

    /**
     * 用户名验证
     * @param username
     * @return
     */
    @Override
    public BaseResponVO userCheck(String username) {

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",username);
        Integer count = userTMapper.selectCount(mtimeUserTEntityWrapper);
        if (count != 0){
            return new BaseResponVO(1,null,null,null,null,"用户已经注册");
        }

        return new BaseResponVO(0,null,null,null,null,"用户名不存在");
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public UserVO auth(String username, String password) {

        if (username == null && password == null){
            return null;
        }

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",username);
        List<MtimeUserT> mtimeUserTS = userTMapper.selectList(mtimeUserTEntityWrapper);
        if (mtimeUserTS == null){
            return null;
        }
        UserVO userVO = new UserVO();
        for (MtimeUserT mtimeUserT : mtimeUserTS) {
            if (password.equals(mtimeUserT.getUserPwd())){
                BeanUtils.copyProperties(mtimeUserT,userVO);
                return userVO;
            }
        }
        return null;
    }
}
