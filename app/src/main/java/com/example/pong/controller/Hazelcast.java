package com.example.pong.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.Ping;
import com.example.Pong;
import com.example.pong.service.PongService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "pong.hazelcast", havingValue = "true")
public class Hazelcast implements MessageListener<Ping> {

    private String key;

    @Autowired
    private HazelcastInstance hazelcast;

    @Autowired
    private PongService service;

    @PostConstruct
    public void init() {
        ITopic<Ping> topic = hazelcast.getTopic(Ping.QUEUE);
        key = topic.addMessageListener(this);
    }

    @PreDestroy
    public void destroy() {
        hazelcast.getTopic(Ping.QUEUE).removeMessageListener(key);
    }

    @Override
    public void onMessage(Message<Ping> message) {
        hazelcast.getTopic(Pong.QUEUE).publish(service.onPing(message.getMessageObject()));
    }
}
