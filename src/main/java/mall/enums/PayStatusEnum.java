package mall.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum {
    PAY_CANCEL(0,"订单取消"),
    PAY_UNPAY(10,"未付款"),
    PAY_PAYED(20,"已付款"),
    PAY_SEND(40,"已发货"),
    PAY_FINISH(50,"交易完成"),
    PAY_CLOST(60,"交易关闭"),
    ;
    Integer code;
    String desc;

    PayStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
