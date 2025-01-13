package com.emergency.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FlotteAttribueeEvent extends ApplicationEvent {
    private final Long flotteId;

    public FlotteAttribueeEvent(Object source, Long flotteId) {
        super(source);
        this.flotteId = flotteId;
    }
}

