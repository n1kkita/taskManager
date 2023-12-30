package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.TaskDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WebSocketController {

    @MessageMapping("/addTask/{groupId}")
    @SendTo("/topic/newTask/{groupId}")
    public TaskDto saveTask(@DestinationVariable Long groupId,
                            @RequestBody TaskDto taskDto){
        return taskDto;
    }

    @MessageMapping("/deleteTask/{taskId}/{groupId}")
    @SendTo("/topic/deleteTask/{groupId}")
    public Long deleteTask(@DestinationVariable Long taskId,
                           @DestinationVariable Long groupId){
        return taskId;
    }
    @MessageMapping("/completeTask/{taskId}/{groupId}")
    @SendTo("/topic/completeTask/{groupId}")
    public Long completeTask(@DestinationVariable Long taskId,
                             @DestinationVariable Long groupId){
        return taskId;
    }

    @MessageMapping("/updateTask/{groupId}")
    @SendTo("/topic/updateTask/{groupId}")
    public TaskDto updateTaskDate(@DestinationVariable Long groupId,
                                  @RequestBody TaskDto taskDto){
        return taskDto;
    }

    /*@MessageMapping("/notification/{userId}")
    @SendTo("/topic/notification/{userId}")
    public Long notificationOfUser(@DestinationVariable Long userId){
        return taskId;
    }*/
}


