package com.example.pong.controller;

import com.example.Ping;
import com.example.PongApi;
import com.example.pong.client.PingClient;
import com.example.pong.service.PongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ConditionalOnProperty(name = "pong.rest", havingValue = "true")
public class Rest implements PongApi {

    @Autowired
    private PingClient rest;

    @Autowired
    private PongService service;

    @Override
    public void ping(Ping ping) {
        rest.pong(service.onPing(ping));
    }

}
