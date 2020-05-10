package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    /*@JmsListener(destination = JmsConfig.MY_QUEUE)
    public void consumer(@Payload HelloWorldMessage helloWorldMessage,
                         @Headers MessageHeaders headers,
                         Message message) {
      log.info("i got a message!!!");
      log.info(helloWorldMessage.toString());
    }*/

    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void consumerHello(@Payload HelloWorldMessage helloWorldMessage,
                         @Headers MessageHeaders headers,
                         Message message) throws JMSException {

        var payload = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world.")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payload);
        log.info("i got a message!!!");
        log.info(helloWorldMessage.toString());
    }
}
