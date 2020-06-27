package com.christianoette.dontchangeit;

import com.christianoette.dontchangeit.services.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/events")
public class ServerSentEventController {

    private static final Logger LOGGER = LogManager.getLogger(ServerSentEventController.class);

    private final EventHandler eventHandler;

    public ServerSentEventController(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @GetMapping("/ping")
    public void emit() {
        eventHandler.pingAllClients();
    }

    @GetMapping("/register-client")
    public SseEmitter sseEmitter() {
       return eventHandler.registerClient();
    }


}
