package guru.springframework.sfgjms.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldMessage implements Serializable {

    private static final long serialVersionUID = 9054041690454724442L;
    private UUID id;
    private String message;
}
