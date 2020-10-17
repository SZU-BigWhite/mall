package mall.service.Impl;

import com.google.gson.Gson;
import mall.dao.CouponMapper;
import mall.enums.ResponseEnum;
import mall.pojo.Coupon;
import mall.pojo.UserCoupon;
import mall.service.ICouponService;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CouponServiceImpl implements ICouponService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    AmqpTemplate amqpTemplate;
    //todo delete
    @Autowired
    RabbitTemplate rabbitTemplate;
    private final String couponId="COUPON_PRODUCTID%d_%d";
    private final String couponLock="COUPON_PRODUCTID%d_LOCK%d";
    private final String couponTimes="COUPON_PRODUCTID%d_TIMES";
    private final String couponNotify="couponNotify";
    public ResponseVo sendCoupon(Integer productId, int num, int times, long amount) {
        //设置优惠券在数据库
        Coupon coupon=new Coupon();
        coupon.setNum(num*times);
        coupon.setProductid(productId);
        coupon.setDecrease(BigDecimal.valueOf(amount));
        int check = couponMapper.countByProductId(productId);
        if(check==0)
            couponMapper.insertSelective(coupon);
        else
            couponMapper.updateByProductId(coupon);


        //同步redis
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(String.format(couponTimes,productId),times+"");
        for(int i=0;i<times;i++){
            String key=String.format(couponId,productId,i);
            opsForValue.set(key,num+"");
        }
        return ResponseVo.success(ResponseEnum.COUPON_SUCCESS);
    }

    @Override
    public ResponseVo getCoupon(Integer productId, int userId) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        //获取锁性能提升times;
        Integer times = Integer.valueOf(opsForValue.get(String.format(couponTimes, productId)));

        //判断库存
        boolean checkStock=false;
        for(int i=0;i<times;i++){
            Integer stockTemp = Integer.valueOf(opsForValue.get(String.format(couponId, productId, i)));
            if(stockTemp>0){
                checkStock=true;
                break;
            }
        }
        if(!checkStock)
            return ResponseVo.error(ResponseEnum.COUPON_EMPTY);

        //获得优惠券redis锁
        boolean checkLock=false;
        String lockKey=null;
        String stockId=null;
        Integer stockNum=0;
        for(int i=0;i<times;i++) {

            //先获得锁，然后再获得库存
            lockKey=String.format(couponLock,productId,i);
            checkLock = opsForValue.setIfAbsent(lockKey, userId + "",10, TimeUnit.MINUTES);
            if(checkLock) {

                stockId = String.format(couponId, productId, i);
                stockNum = Integer.valueOf(opsForValue.get(stockId));
                //库存不足，释放锁
                if(stockNum==0) {
                    redisTemplate.delete(lockKey);
                    checkLock=false;
                    continue;
                }
                break;
            }
        }
        if(!checkLock)
            return ResponseVo.error(ResponseEnum.COUPON_TIME_OUT);

        try {
            //库存-1
            stockNum--;
            opsForValue.set(stockId,stockNum+"");

            //消息队列通知数据库操作
            UserCoupon userCoupon=new UserCoupon();
            userCoupon.setProductid(productId);
            userCoupon.setUserid(userId);
            amqpTemplate.convertAndSend(couponNotify,new Gson().toJson(userCoupon));

            //后台输出提示信息
            String resMsg="用户"+userId+"获得优惠券";
            System.out.println(resMsg);
            System.out.println(stockId+"剩余库存:"+stockNum);

            return ResponseVo.successByMsg(resMsg);
        }catch (Exception e){
            throw new RuntimeException("操作失败");
        }finally {
            //释放锁
            redisTemplate.delete(lockKey);
        }
    }

}
