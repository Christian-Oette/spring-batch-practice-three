package com.christianoette.dontchangeit.services;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class EventClient {
    private SseEmitter emitter;

    public EventClient(SseEmitter emitter) {
        this.emitter = emitter;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }
}

