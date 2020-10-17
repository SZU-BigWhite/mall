package mall.service;


import mall.form.CartAddForm;
import mall.form.CartUpdateForm;
import mall.pojo.Cart;
import mall.vo.CartVo;
import mall.vo.ResponseVo;

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
