package mall.service.Impl;

import mall.MallApplicationTests;
import mall.enums.RoleEnum;
import mall.pojo.User;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//数据库回滚，单测不污染数据库
@Transactional
@Slf4j
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USERNAME="admin1";
    public static final String PASSWORD="admin1";
    @Autowired
    private UserServiceImpl userService;
    @Before
    public void register() {
        User user=new User(USERNAME,PASSWORD,"Tom@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }
    @Test
    public void login(){
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        log.info("status={}{}",responseVo.getStatus(),responseVo.getMsg());
    }
}