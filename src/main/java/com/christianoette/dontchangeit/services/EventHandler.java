package com.christianoette.dontchangeit.services;

import com.christianoette.dontchangeit.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Component
public class EventHandler {

    private static final Logger LOGGER = LogManager.getLogger(EventHandler.class);
    private List<EventClient> notifiers = new ArrayList<>();
    private static final boolean logPingEvents = false;
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    @Scheduled(fixedDelay = 2000, initialDelay = 2000)
    public void pingAllClients() {
        int id = idCounter.incrementAndGet();
        SseEmitter.SseEventBuilder eventBuilder = event().name("ping").id(String.valueOf(id))
                .data((Event) () -> String.valueOf("Ping"), MediaType.APPLICATION_JSON);
        sendEvent(eventBuilder);
    }

    public void notifyNewJob() {
        int id = idCounter.incrementAndGet();
        SseEmitter.SseEventBuilder eventBuilder = event().name("jobs").id(String.valueOf(id))
                .data((Event) () -> String.valueOf("New Job created"));
        sendEvent(eventBuilder);
    }

    private void sendEvent(SseEmitter.SseEventBuilder eventBuilder) {
        if (notifiers.size() == 0) {
            LOGGER.info("No clients connected");
        }
        if (logPingEvents) {
            LOGGER.info("Send event, Currently connected {} ", notifiers.size());
        }
        try {
            for (EventClient client : notifiers) {
                try {

                    client.getEmitter().send(eventBuilder);
                } catch (IOException e) {
                    client.getEmitter().completeWithError(e);
                }
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public SseEmitter registerClient() {
        LOGGER.info("Client registered");
        SseEmitter notifier = new SseEmitter(Long.MAX_VALUE);
        EventClient client = new EventClient(notifier);
        notifiers.add(client);
        notifier.onCompletion(() -> notifiers.remove(client));
        notifier.onError((err) -> notifiers.remove(client));
        notifier.onTimeout(() -> notifiers.remove(client));
        return notifier;
    }
}
