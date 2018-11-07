package com.example.pong.service;

import com.example.Ping;
import com.example.Pong;
import com.example.exception.PongException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PongService {

    public Pong onPing(Ping ping) {
        log.info("onPing() received {}", ping);
        if (ping == null || ping.getUuid() == null) {
            throw new PongException("Pong model did not contain an uuid.");
        }
        return new Pong()
            .setUuid(ping.getUuid())
            .setProtocol(ping.getProtocol());
    }

}
