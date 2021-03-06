package com.imooc.mall.service.Impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.vo.ResponseVo;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    CartServiceImpl cartService;
    private Gson gson=new GsonBuilder().setPrettyPrinting().create();
    Integer userId=2;
    Integer productId=27;
    @Before
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(productId);
        cartAddForm.setSelected(true);
        ResponseVo add = cartService.add(userId, cartAddForm);
        log.info("cartVo={}",gson.toJson(add));
    }
    @Test
    public void createTest() {
        ResponseVo responseVo=orderService.create(2,6);
        log.info("result={}",gson.toJson(responseVo));
    }
    public ResponseVo<OrderVo> create() {
        ResponseVo responseVo=orderService.create(2,6);
        log.info("result={}",gson.toJson(responseVo));
        return responseVo;
    }
    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = orderService.list(2, 1, 3);
        log.info("result={}",gson.toJson(responseVo));
    }
    @Test
    public  void detail(){
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.detail(userId, vo.getData().getOrderNo());
        log.info("responseVo={}",gson.toJson(responseVo));
    }
    @Test
    public  void cancel(){
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.cancel(userId, vo.getData().getOrderNo());
        log.info("responseVo={}",gson.toJson(responseVo));
    }

}