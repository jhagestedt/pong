package com.example.pong.rest;

import com.example.ping.Ping;
import com.example.pong.Pong;
import com.example.pong.PongApi;
import com.example.pong.service.PongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PongRest implements PongApi {

    @Autowired
    private PongService service;

    @Override
    public Pong pong(Ping ping) {
        return service.pong(ping);
    }

}
