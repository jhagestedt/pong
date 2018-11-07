package com.example.pong.client;

import com.example.PingApi;
import com.example.Pong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(
    name = "ping",
    fallback = PingClient.Fallback.class
)
public interface PingClient extends PingApi {

    @Slf4j
    @Component
    class Fallback implements PingClient {

        @Override
        public void pong(Pong pong) {
            log.warn("pong() fallback used for {}", pong);
        }
    }

}
