package mall.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicListener {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "topic",type = "topic"),
                    key = {"user.*"}
            )
    })
    public void getMsg1(String Msg){
        System.out.println("user.*:"+Msg);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "topic",type = "topic"),
                    key = {"user.#"}
            )
    })
    public void getMsg2(String Msg){
        System.out.println("user.#:"+Msg);
    }
}
