package com.example.pong.controller;

import com.example.Ping;
import com.example.pong.service.PongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

@Controller
@EnableBinding({
    Eventhub.PingProcessor.class,
    Eventhub.PongProcessor.class
})
public class Eventhub {

    @Autowired
    private PongService pongService;

    @Autowired
    private PongProcessor pongProcessor;

    @StreamListener(PingProcessor.PING)
    public void ping(Ping ping) {
        pongProcessor.pong().send(MessageBuilder
            .withPayload(pongService.onPing(ping))
            .build());
    }

    public interface PingProcessor {

        String PING = "ping";

        @Input
        SubscribableChannel ping();

    }

    public interface PongProcessor {

        String PONG = "pong";

        @Output
        MessageChannel pong();

    }
}
