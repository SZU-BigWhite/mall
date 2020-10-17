package mall.service;

import com.github.pagehelper.PageInfo;
import mall.vo.OrderVo;
import mall.vo.ResponseVo;

public interface IOrderService {
    ResponseVo<OrderVo> create(Integer userId, Integer shippingId);
    ResponseVo<PageInfo> list(Integer userId, Integer pageSize, Integer pageNum);
    ResponseVo<OrderVo> detail(Integer userId,Long orderNo);
    ResponseVo cancel(Integer userId,Long orderNo);
    void paid(Long orderNo);
}
