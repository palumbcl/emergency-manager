package com.emergency.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import com.emergency.model.Capteur;
import java.util.List;

@Getter
public class CapteursUpdatedEvent extends ApplicationEvent {
    private final List<Capteur> capteurs;

    public CapteursUpdatedEvent(Object source, List<Capteur> capteurs) {
        super(source);
        this.capteurs = capteurs;
    }
}

