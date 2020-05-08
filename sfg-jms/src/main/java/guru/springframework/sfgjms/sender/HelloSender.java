package guru.springframework.sfgjms.sender;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedDelay = 2000)
    public void sendMessage() {
      log.info("Sendind a message");
      var message = HelloWorldMessage.builder()
              .id(UUID.randomUUID())
              .message("Hello world.")
              .build();

      jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
      log.info("Message sent!");
    }
}
