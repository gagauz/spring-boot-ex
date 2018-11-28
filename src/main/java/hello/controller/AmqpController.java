package hello.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AmqpController {
    @RequestMapping(path = "/amqp/send", method = GET)
    public void send() throws IOException {

    }
}
