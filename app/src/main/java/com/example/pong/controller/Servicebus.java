package com.example.pong.controller;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.Ping;
import com.example.Pong;
import com.example.pong.service.PongService;
import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "pong.servicebus", havingValue = "true")
public class Servicebus implements IMessageHandler {

    private Gson gson = new Gson();

    @Autowired
    private QueueClient pingQueue;

    @Autowired
    private QueueClient pongQueue;

    @Autowired
    private PongService pongService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        try {
            pingQueue.registerMessageHandler(this,
                new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),
                executorService);
        } catch (ServiceBusException e) {
            log.error("init() error at ping init", e);
        } catch (InterruptedException e) {
            log.error("init() general error at ping init", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            pingQueue.close();
        } catch (ServiceBusException e) {
            log.error("destroy() error at ping destroy", e);
        }
        executorService.shutdown();
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage in) {
        Ping ping = gson.fromJson(new String(in.getBody(), StandardCharsets.UTF_8), Ping.class);

        Pong pong = pongService.onPing(ping);

        Message message = new Message();
        message.setBody(gson.toJson(pong).getBytes(StandardCharsets.UTF_8));
        message.setContentType(MediaType.APPLICATION_JSON_VALUE);
        message.setLabel(Pong.QUEUE);
        message.setMessageId(pong.getUuid());
        message.setTimeToLive(Duration.ofMinutes(2));

        try {
            pongQueue.send(message);
        } catch (ServiceBusException e) {
            log.error("onMessageAsync() error at pong send", e);
        } catch (InterruptedException e) {
            log.error("onMessageAsync() general error at pong send", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void notifyException(Throwable throwable, ExceptionPhase phase) {
        log.error("notifyException() error occurred.", throwable);
    }
}
