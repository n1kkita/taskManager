package com.example.taskmanager.events;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class RemoveCodeEvent extends ApplicationEvent {
    private final HttpSession session;
    public RemoveCodeEvent(String link, HttpSession session) {
        super(link);
        this.session = session;
    }
}
