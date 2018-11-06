package com.example.pong.service;

import com.example.exception.PongException;
import com.example.ping.Ping;
import com.example.pong.Pong;
import com.example.pong.PongApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PongService implements PongApi {

    @Override
    public Pong pong(Ping ping) {
        log.info("pong() received {}", ping);
        if (ping == null || ping.getUuid() == null) {
            throw new PongException("Pong model did not contain an uuid.");
        }
        return new Pong()
            .setUuid(ping.getUuid())
            .setProtocol(ping.getProtocol());
    }

}
