package com.dummybot.graphql.service;

import com.dummybot.graphql.repositories.Tasklist;
import com.dummybot.graphql.repositories.TasklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasklistService {

    private final TasklistRepository tasklistRepository;

    public List<Tasklist> getTaskListByAssignee(String assignee){
        return tasklistRepository.findAllByAssignedUser(assignee);
    }

    public Iterable<Tasklist> getAllTaskLists(){
        return tasklistRepository.findAll();
    }

    public List<Tasklist> completeTask(Long id){
        Optional<Tasklist> tasklistOptional = tasklistRepository.findById(id);
        if (tasklistOptional.isPresent()) {
            tasklistRepository.deleteById(id);
            return getTaskListByAssignee(tasklistOptional.get().getAssignedUser());
        }
        return new ArrayList<>();
    }


}
