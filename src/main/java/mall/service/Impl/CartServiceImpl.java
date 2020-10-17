package mall.service.Impl;

import mall.form.CartAddForm;
import com.google.gson.Gson;
import mall.dao.ProductMapper;
import mall.enums.ResponseEnum;
import mall.form.CartUpdateForm;
import mall.pojo.Cart;
import mall.pojo.Product;
import mall.service.ICartService;
import mall.vo.CartProductVo;
import mall.vo.CartVo;
import mall.vo.ResponseVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements ICartService {
    private final static String UID_TEMPLATE="UID_%d";
    @Autowired
    ProductMapper productMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    Gson gson=new Gson();
    public ResponseVo add(Integer userId, CartAddForm cartAddForm) {
        Integer productNum=1;
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        //商品存在
        if(product==null){
            return ResponseVo.error(ResponseEnum.PRODUCT_404);
        }
        //商品在售
        if(product.getStatus()!=1) {
            return  ResponseVo.error(ResponseEnum.OUT_SHOP_OR_DELETE);
        }
        //库存充足
        if(product.getStock()<=0){
            return ResponseVo.error(ResponseEnum.PRODUCT_EMPTY);
        }
        //TODO 使用redis缓存购物车
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        //写入redis (用hash方式--高性能)
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(UID_TEMPLATE, userId);
        String value=opsForHash.get(redisKey,String.valueOf(product.getId()));
        Cart cart;
        if(StringUtils.isNullOrEmpty(value)){
            //新增商品
            cart = new Cart(product.getId(), productNum, cartAddForm.isSelected());
        }else{
            //数量+1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity()+productNum);
        }

        opsForHash.put(String.format(UID_TEMPLATE, userId),
                String.valueOf(product.getId()),
                gson.toJson(cart));

        return list(userId);
    }
    public ResponseVo<CartVo> list(Integer userId){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(UID_TEMPLATE, userId);
        Map<String, String> entries = opsForHash.entries(redisKey);

        List<CartProductVo> cartProductVoList=new ArrayList<>();
        BigDecimal cartTotalPrice=BigDecimal.valueOf(1);
        boolean selectedAll=true;
        Integer cartTotalQuantity=0;
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            int pid = Integer.valueOf(entry.getKey());
            String val=entry.getValue();
            Cart cart=gson.fromJson(val,Cart.class);

            Product product = productMapper.selectByPrimaryKey(pid);
            if(product==null)
                continue;
            CartProductVo cartProductVo=new CartProductVo(product.getId(),cart.getQuantity(),
                    product.getName(),product.getSubtitle(),product.getMainImage(),product.getPrice(),
                    product.getStatus(),product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                    product.getStock(),cart.getProductSelected());

            if(cart.getProductSelected()==false)
                selectedAll=false;
            //总计 选中
            if(cart.getProductSelected()){
                cartTotalPrice=cartTotalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                cartTotalQuantity+=cart.getQuantity();
            }

            cartProductVoList.add(cartProductVo);
        }

        CartVo cartVo=new CartVo();
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setSelectedAll(true);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);

        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer userId, Integer productId, CartUpdateForm cartUpdateForm) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey=String.format(UID_TEMPLATE,userId);
        Map<String, String> entries = opsForHash.entries(redisKey);

        String value=entries.get(String.valueOf(productId));
        //商品不存在
        if(value.isEmpty())
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_EMPTY);

        CartProductVo cartProductVo=gson.fromJson(value,CartProductVo.class);
        if(cartUpdateForm.getQuantity()>=0)
            cartProductVo.setQuantity(cartUpdateForm.getQuantity());
        if(cartUpdateForm.getSelected()!=null)
            cartProductVo.setProductSelected(cartUpdateForm.getSelected());
        //添加进商品
        opsForHash.put(redisKey,String.valueOf(cartProductVo.getProductId()),gson.toJson(cartProductVo));
        return list(userId);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer userId, Integer productId) {
        String redisKey=String.format(UID_TEMPLATE,userId);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);

        String value=entries.get(String.valueOf(productId));
        //商品不存在
        if(StringUtils.isNullOrEmpty(value))
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_EMPTY);

        opsForHash.delete(redisKey,String.valueOf(productId));
        return list(userId);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer userId) {
        String redisKey=String.format(UID_TEMPLATE,userId);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);

        for (String value : entries.values()) {
            Cart cart=gson.fromJson(value,Cart.class);
            cart.setProductSelected(true);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(userId);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer userId) {
        String redisKey=String.format(UID_TEMPLATE,userId);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);

        for (String value : entries.values()) {
            Cart cart=gson.fromJson(value,Cart.class);
            cart.setProductSelected(false);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(userId);
    }

    @Override
    public ResponseVo<Integer> getCartProductsSum(Integer userId) {
        String redisKey=String.format(UID_TEMPLATE,userId);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);

        //计算总数
        Integer totalQuantity=0;
        List<Cart> carts = cartList(userId);
        for (Cart cart : carts) {
            if(cart.getProductSelected())
                totalQuantity+=cart.getQuantity();
        }
        return ResponseVo.success(totalQuantity);
    }
    public List<Cart> cartList(Integer userId){
        String redisKey=String.format(UID_TEMPLATE,userId);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> entries = opsForHash.entries(redisKey);
        List<Cart> res=new ArrayList<>();
        for (String value : entries.values()) {
            Cart cart=gson.fromJson(value,Cart.class);
            res.add(cart);
        }
        return res;
    }
}
