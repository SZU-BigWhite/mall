package mall;

import mall.exception.UserLoginException;
import mall.consts.MallConst;
import mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * true-继续流程  false-中断流程
     */

    //TODO 登录拦截器 抛出登录异常
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user=(User)request.getSession().getAttribute(MallConst.CURRENT_USER);
        if(user==null){
            log.info("user=null");
            throw new UserLoginException();
        }
        return true;
    }
}
