package vip.testops.coffee.order.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageClient {
    String OUTPUT = "coffeeOut";
    String INPUT = "coffeeIn";

    @Output(MessageClient.OUTPUT)
    MessageChannel output();

}
