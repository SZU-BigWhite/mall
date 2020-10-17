package mall.enums;

import lombok.Getter;

/**
 * 0-admin 1-customer
 */
@Getter
public enum RoleEnum {
    ADMIN(0),
    CUSTOMER(1),
    ;
    Integer code;
    RoleEnum(Integer code)
    {
        this.code=code;
    }
}
