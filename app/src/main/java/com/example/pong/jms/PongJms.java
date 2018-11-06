package com.example.pong.jms;

import com.example.ping.Ping;
import com.example.pong.Pong;
import com.example.pong.service.PongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PongJms {

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private PongService service;

    @JmsListener(destination = Ping.QUEUE)
    public void onPing(Ping ping) {
        jms.convertAndSend(Pong.QUEUE, service.pong(ping));
    }

}
