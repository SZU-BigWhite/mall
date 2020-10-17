package com.service.Impl;

import com.MallApplicationTests;
import mall.form.CartAddForm;
import mall.form.CartUpdateForm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mall.service.Impl.CartServiceImpl;
import mall.vo.CartVo;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CartServiceImplTest extends MallApplicationTests {

    private Gson gson=new GsonBuilder().setPrettyPrinting().create();
    @Autowired
    CartServiceImpl cartService;
    Integer userId=2;
    Integer productId=26;
    @Before
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(productId);
        cartAddForm.setSelected(true);
        ResponseVo add = cartService.add(userId, cartAddForm);
        log.info("cartVo={}",gson.toJson(add));
    }
    @Test
    public void list(){
        ResponseVo<CartVo> list = cartService.list(userId);
        log.info("cartVo={}",gson.toJson(list));

    }
    @Test
    public void update(){
        CartUpdateForm cartUpdateForm=new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        ResponseVo<CartVo> list = cartService.update(userId,productId,cartUpdateForm);
        log.info("cartVo={}",gson.toJson(list));
    }
    @Test
    public void delete(){
        ResponseVo<CartVo> list = cartService.delete(userId,productId);
        log.info("cartVo={}",gson.toJson(list));
    }
    @Test
    public void selectAll(){
        ResponseVo<CartVo> list = cartService.selectAll(userId);
        log.info("cartVo={}",gson.toJson(list));
    }
    @Test
    public void unSelectAll(){
        ResponseVo<CartVo> list = cartService.unSelectAll(userId);
        log.info("cartVo={}",gson.toJson(list));
    }
    @Test
    public void getCartProductsSum(){
        ResponseVo<Integer> list = cartService.getCartProductsSum(userId);
        log.info("cartVo={}",gson.toJson(list));
    }

}
