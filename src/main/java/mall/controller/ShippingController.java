package mall.controller;

import mall.consts.MallConst;
import mall.form.ShippingAddForm;
import mall.form.ShippingGetForm;
import mall.form.ShippingUpdateForm;
import mall.pojo.User;
import mall.service.Impl.ShippingServiceImpl;
import mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class ShippingController {
    @Autowired
    ShippingServiceImpl shippingService;
    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingAddForm shippingAddForm,
                          HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.add(user.getId(),shippingAddForm);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable("shippingId") Integer shippingId){
        return shippingService.delete(shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable("shippingId") Integer shippingId,
                             @Valid @RequestBody ShippingUpdateForm shippingUpdateForm){
        return shippingService.update(shippingId,shippingUpdateForm);
    }
    @GetMapping("/shippings")
    public ResponseVo get(@RequestBody(required = false) ShippingGetForm shippingGetForm,
                          HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.get(user.getId(),shippingGetForm);
    }

}
