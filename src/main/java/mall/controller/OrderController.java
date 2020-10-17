package mall.controller;

import mall.form.OrderCreateForm;
import com.github.pagehelper.PageInfo;
import mall.consts.MallConst;
import mall.pojo.User;
import mall.service.Impl.OrderServiceImpl;
import mall.vo.OrderVo;
import mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class OrderController {
    @Autowired
    OrderServiceImpl orderService;
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession httpSession){
        User user=(User)httpSession.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(),form.getShippingId());
    }
    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam Integer pageSize,
                                     @RequestParam Integer pageNum,
                                    HttpSession httpSession){
        User user=(User)httpSession.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(),pageNum,pageSize);
    }
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
                                    HttpSession httpSession){
        User user=(User)httpSession.getAttribute(MallConst.CURRENT_USER);
        return orderService.detail(user.getId(),orderNo);
    }
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable Long orderNo,
                             HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(MallConst.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }

}
