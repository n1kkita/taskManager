package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.GroupHistory;

import java.util.List;

public interface GroupHistoryService {
    void save(GroupEntity group, String text);
    List<GroupHistory> getAllByGroup(GroupEntity group);
}
