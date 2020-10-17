package mall.service.Impl;

import mall.enums.RoleEnum;
import mall.dao.UserMapper;
import mall.enums.ResponseEnum;
import mall.pojo.User;
import mall.service.IUserService;
import mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;
    /**
     * 注册
     */
    public ResponseVo<User> register(User user) {

        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername>0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail>0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        //密码加密，spring自带MD摘要
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(password);

        user.setRole(RoleEnum.CUSTOMER.getCode());
        //写入数据库
        int countInsert = userMapper.insertSelective(user);
        if(countInsert==0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 登录用户
     * @param username
     * @param password
     * @return
     */
    public ResponseVo<User> login(String username,String password){
        User user = userMapper.selectByUsername(username);
        if(user==null){
            //用户不存在
            return ResponseVo.error(ResponseEnum.PASSWORD_OR_USERNAME_ERROR);
        }
        String check = DigestUtils.md5DigestAsHex(password.getBytes(Charset.forName("utf-8")));
        if(!check.equalsIgnoreCase(user.getPassword())){
            //密码错误
            return ResponseVo.error(ResponseEnum.PASSWORD_OR_USERNAME_ERROR);
        }
        user.setPassword("");
        return ResponseVo.success(user);

    }



}
