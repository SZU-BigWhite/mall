package com.imooc.mall.service.Impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.vo.ResponseVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CouponServiceImplTest extends MallApplicationTests {
    Gson gson=new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    CouponServiceImpl couponService;
    @Test
    public void sendCoupon() {
        ResponseVo responseVo = couponService.sendCoupon(123789, 50, 5,50);
        log.info("res={}",gson.toJson(responseVo));
    }

    @Test
    public void getCoupon() {
        ResponseVo responseVo = couponService.getCoupon(123123, 3194);
        log.info("res={}",gson.toJson(responseVo));
    }
}