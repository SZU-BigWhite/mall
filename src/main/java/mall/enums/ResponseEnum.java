package mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    ERROR(-1,"服务端错误"),
    SUCCESS(0,"成功"),
    COUPON_SUCCESS(0,"优惠券添加成功"),
    COUPON_TIME_OUT(1,"系统繁忙,请重试"),
    COUPON_EMPTY(1,"优惠券不足"),
    PASSWORD_ERROR(1,"密码错误"),
    USERNAME_EXIST(2,"用户已存在"),
    EMAIL_EXIST(4,"邮箱已存在"),
    PARAM_ERROR(3,"参数错误"),
    NEED_LOGIN(10,"用户未登录,请先登录"),
    PASSWORD_OR_USERNAME_ERROR(11,"用户名或密码错误"),
    OUT_SHOP_OR_DELETE(12,"下架或已删除"),
    PRODUCT_404(13,"商品不存在"),
    PRODUCT_EMPTY(14,"商品库存不足"),
    CART_PRODUCT_EMPTY(15,"购物车不存在该商品"),
    SHIPPING_EMPTY(1,"删除地址失败"),
    ORDER_SHIPPING_EMPTY(1,"收货地址不存在"),
    CART_EMPTY(1,"购物车没有订单"),



    ;
    Integer code;
    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
