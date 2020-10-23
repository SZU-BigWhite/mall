package com.imooc.mall.service;


import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;

import java.util.List;

public interface ICartService {
    ResponseVo<CartVo> add(Integer userId, CartAddForm cartAddForm);
    ResponseVo<CartVo> list(Integer userId);
    ResponseVo<CartVo> update(Integer userId, Integer productId, CartUpdateForm cartUpdateForm);
    ResponseVo<CartVo> delete(Integer userId,Integer productId);
    ResponseVo<CartVo> selectAll(Integer userId);
    ResponseVo<CartVo> unSelectAll(Integer userId);
    ResponseVo<Integer> getCartProductsSum(Integer userId);
    List<Cart> cartList(Integer userId);
}
