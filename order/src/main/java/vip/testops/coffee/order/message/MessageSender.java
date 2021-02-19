package vip.testops.coffee.order.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MessageClient.class)
public class MessageSender {
    private MessageChannel output;

    @Autowired
    @Qualifier(MessageClient.OUTPUT)
    public void setOutput(MessageChannel output) {
        this.output = output;
    }

    public void send(Object object){
        output.send(MessageBuilder.withPayload(object).build());
    }
}
