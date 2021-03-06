package guru.sfg.beer.inventory.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class JmsConfig {

    public static final String NEW_INVENTORY_QUEUE = "new-inventory";

    @Bean
    public MessageConverter messageConverter(ObjectMapper mapper) {
        MappingJackson2MessageConverter jackson2MessageConverter  = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setTargetType(MessageType.TEXT);
        jackson2MessageConverter.setTypeIdPropertyName("_type");
        jackson2MessageConverter.setObjectMapper(mapper);
        return jackson2MessageConverter;
    }
}
