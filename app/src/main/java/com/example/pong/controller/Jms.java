package com.example.pong.controller;

import com.example.Ping;
import com.example.Pong;
import com.example.pong.service.PongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "pong.jms", havingValue = "true")
public class Jms {

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private PongService service;

    @JmsListener(destination = Ping.QUEUE)
    public void onPing(Ping ping) {
        jms.convertAndSend(Pong.QUEUE, service.onPing(ping));
    }

}
