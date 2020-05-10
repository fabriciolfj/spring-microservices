package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 2000)
    public void sendMessage() {
      var message = HelloWorldMessage.builder()
              .id(UUID.randomUUID())
              .message("Hello world.")
              .build();

      jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }

    @Scheduled(fixedDelay = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        log.info("Sendind a message");
        var message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world.")
                .build();

        Message receiveMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            Message helloMessage = null;

            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");

                log.info("Sendming hello");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        log.info("Receive message produced.");
        log.info(receiveMessage.getBody(String.class));

    }
}
