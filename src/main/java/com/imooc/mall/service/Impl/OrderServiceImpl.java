package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.PayStatusEnum;
import com.imooc.mall.enums.PayTypeEnum;
import com.imooc.mall.enums.ProdustEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderItemVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer userId, Integer shippingId) {
        //查收货地址
        Shipping shipping = shippingMapper.selectByIdAndUserId(shippingId, userId);
        if(shipping==null)
            return ResponseVo.error(ResponseEnum.ORDER_SHIPPING_EMPTY);

        //校验库存 + 计算选中商品的总价
        List<Cart> cartList = cartService.cartList(userId).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        //购物车为空
        if(cartList.isEmpty())
            return ResponseVo.error(ResponseEnum.CART_EMPTY);

        Set<Integer> productSet = cartList.stream()
                .map(cart -> cart.getProductId())
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectProductIdSets(productSet);
        //商品全部下架
        if(productList.isEmpty())
            return ResponseVo.error(ResponseEnum.PRODUCT_404);

        Map<Integer,Product> productMap=productList.stream()
                .collect(Collectors.toMap(cart->cart.getId(),product -> product));

        Long orderNo=generateOrderNo();
        List<OrderItem> orderItemList=new ArrayList<>();
        for (Cart cart : cartList) {
            Product product=productMap.get(cart.getProductId());
            //商品不存在
            if(product==null)
                return ResponseVo.error(ResponseEnum.PRODUCT_404);
            //商品下架/不存在
            if(!product.getStatus().equals(ProdustEnum.ON_SALE.getCode()))
                return ResponseVo.error(ResponseEnum.PRODUCT_404);
            //商品库存不足
            if(product.getStock()<cart.getQuantity())
                return ResponseVo.error(ResponseEnum.PRODUCT_EMPTY);

            //计算选中商品的总价
            //生成订单  入库 order 和 order_item（事务）
            OrderItem orderItem = orderItemBuild(userId, orderNo, product, cart.getQuantity());
            orderItemList.add(orderItem);

            //库存减少
            product.setStock(product.getStock()-cart.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        //订单写入数据库
        Order order=orderBuild(userId,orderNo,shippingId,orderItemList);
        int row = orderMapper.insertSelective(order);
        if(row<=0)
            return ResponseVo.error(ResponseEnum.ERROR);
        row = orderItemMapper.batchInsert(orderItemList);
        if(row<=0)
            return ResponseVo.error(ResponseEnum.ERROR);

        //更新购物车
        for (Cart cart : cartList) {
            cartService.delete(userId,cart.getProductId());
        }
        //构造OrderVo
        OrderVo orderVo = orderVoBuild(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        PageHelper.startPage(pageNum,pageSize);
        //所有的订单
        List<Order> orderList = orderMapper.selectByUserId(userId);
        log.info("orderList={}",gson.toJson(orderList));
        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo)
                .collect(Collectors.toSet());
        //订单里所有的orderItem   map-> order对应orderItemList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderNo));

        //订单里所有的shipping
        Set<Integer> shippingIdSet = orderList.stream().map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream().collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList=new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = orderVoBuild(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order==null||!order.getUserId().equals(userId)){
            return ResponseVo.error(ResponseEnum.CART_EMPTY);
        }

        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = orderVoBuild(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在or订单号错误
        if(order==null||!order.getUserId().equals(userId)){
            return ResponseVo.error(ResponseEnum.CART_EMPTY);
        }
        //设置订单状态（取消）
        order.setStatus(PayStatusEnum.PAY_CANCEL.getCode());
        order.setCloseTime(new Date());
        orderMapper.updateByPrimaryKey(order);

        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在or订单号错误
        if(order==null){
            throw new RuntimeException(ResponseEnum.CART_EMPTY.getDesc());
        }
        //设置订单状态（已付款）
        order.setStatus(PayStatusEnum.PAY_PAYED.getCode());
        order.setPaymentTime(new Date());
        orderMapper.updateByPrimaryKey(order);
    }

    private OrderVo orderVoBuild(Order order,List<OrderItem> orderItemList,Shipping shipping) {
        OrderVo orderVo=new OrderVo();

        BeanUtils.copyProperties(order,orderVo);
        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        if(shipping!=null){
            orderVo.setOrderItemVoList(orderItemVoList);
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    /**
     * 企业级  分布式唯一id
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis()+new Random().nextInt(999);
    }

    private OrderItem orderItemBuild(Integer userId,Long orderNo,Product product,Integer quantity) {
        OrderItem orderItem=new OrderItem();
        orderItem.setUserId(userId);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return orderItem;
    }

    private Order orderBuild(Integer userId, Long orderNo,Integer shippingId, List<OrderItem> orderItemList) {
        Order order=new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //遍历orderItemList
        BigDecimal payment=BigDecimal.valueOf(0);
        for (OrderItem orderItem : orderItemList) {
            payment=payment.add(orderItem.getTotalPrice());
        }
        order.setPayment(payment);
        order.setPaymentType(PayTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(PayStatusEnum.PAY_UNPAY.getCode());
        return order;
    }
}
