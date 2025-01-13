package com.emergency.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FeuDetecteEvent extends ApplicationEvent {
    private final Long feuId;

    public FeuDetecteEvent(Object source, Long feuId) {
        super(source);
        this.feuId = feuId;
    }
}
