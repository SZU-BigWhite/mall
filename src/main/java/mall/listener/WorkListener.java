package mall.listener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkListener {

    @RabbitListener(queuesToDeclare = @Queue("workQueue"))
    public void getMsg1(String msg){
        System.out.println("MSG1："+msg);
    }
    @RabbitListener(queuesToDeclare = @Queue("workQueue"))
    public void getMsg2(String msg){
        System.out.println("MSG2："+msg);
    }
}
