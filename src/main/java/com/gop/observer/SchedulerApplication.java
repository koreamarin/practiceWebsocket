package com.gop.observer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerApplication {

    @Autowired
    WebSocketHandler webSocketHandler;

    @Scheduled(fixedDelay = 1000 * 5)    // 5seconds
    public void run() throws Exception {
        webSocketHandler.onEventTrigger();
    }
}