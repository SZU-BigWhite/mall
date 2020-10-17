package mall.controller;

import mall.service.Impl.CouponServiceImpl;
import mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {
    @Autowired
    CouponServiceImpl couponService;
    @GetMapping("/coupon")
    public ResponseVo getCoupon(@RequestParam Integer productId, @RequestParam Integer userId){
        return couponService.getCoupon(productId,userId);
    }
}
