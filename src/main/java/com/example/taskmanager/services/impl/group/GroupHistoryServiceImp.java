package com.example.taskmanager.services.impl.group;

import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.GroupHistory;
import com.example.taskmanager.repositories.GroupHistoryRepository;
import com.example.taskmanager.services.interfaceses.GroupHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupHistoryServiceImp implements GroupHistoryService {
    private final GroupHistoryRepository groupHistoryRepository;
    @Override
    public void save(GroupEntity group, String text) {
        GroupHistory groupHistory = new GroupHistory();
        groupHistory.setGroup(group);
        groupHistory.setText(text);
        groupHistoryRepository.save(groupHistory);
    }

    @Override
    public List<GroupHistory> getAllByGroup(GroupEntity group) {
        return groupHistoryRepository.findByGroup(group);
    }
}
