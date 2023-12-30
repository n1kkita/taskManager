package com.example.taskmanager.events;

import com.example.taskmanager.dto.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class VerificationEvent extends ApplicationEvent {
    private final Integer verificationCode;
    public VerificationEvent(UserDto userDto, Integer verificationCode) {
        super(userDto);
        this.verificationCode = verificationCode;
    }

}
