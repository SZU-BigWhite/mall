# 商城系统（支付分开)

## 技术实现
  ### springboot+mybatis 购物数据的搭建
    商品类型
    商品数据详细信息
    购物车
    订单
    收获地址
  ### redis 缓存购物车和优惠券
    购物车 采用hashMap的数据结构
    优惠券 采用String类型，加入高并发锁
  ### rabbitMQ 与支付系统、高并发写入数据库操作解耦和降压 
    接收支付系统，完成订单
    优惠券抢购成功发送给mq
  
#### 克隆项目
```
git clone  https://github.com/SZU-BigWhite/mall.git
```
