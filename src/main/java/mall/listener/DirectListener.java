package mall.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectListener {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "directs",type = "direct"),
                    key = {"error"} //绑定接受的routingKey
            )
    })
    public void getError(String msg){
        System.out.println("ErrorMsg:"+msg);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "directs",type = "direct"),
                    key = {"error","logs","warning"}
            )
    })
    public void getAll(String msg){
        System.out.println("AllMsg开始成功:"+msg);
        int temp=10/0;
        System.out.println("AllMsg消费成功:"+msg);
    }

}
