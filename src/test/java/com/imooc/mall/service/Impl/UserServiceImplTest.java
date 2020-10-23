package com.imooc.mall.service.Impl;

import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//数据库回滚，单测不污染数据库
@Transactional
@Slf4j
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USERNAME="admin1";
    public static final String PASSWORD="admin1";
    @Autowired
    private UserServiceImpl userService;
    @Before
    public void register() {
        User user=new User(USERNAME,PASSWORD,"Tom@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }
    @Test
    public void login(){
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        log.info("status={}{}",responseVo.getStatus(),responseVo.getMsg());
    }
}