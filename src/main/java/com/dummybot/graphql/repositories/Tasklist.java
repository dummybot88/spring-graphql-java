package com.dummybot.graphql.repositories;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
@Entity
@Getter
@NoArgsConstructor
public class Tasklist {

    @Id
    @GeneratedValue
    private Long id;

    private String taskType;

    private String userKey;

    private String subject;

    private LocalDate beginDate;

    private LocalDate dueDate;

    private String assignedUser;

    public Tasklist(String taskType, String userKey, String subject, LocalDate beginDate, LocalDate dueDate, String assignedUser) {
        this.taskType = taskType;
        this.userKey = userKey;
        this.subject = subject;
        this.beginDate = beginDate;
        this.dueDate = dueDate;
        this.assignedUser = assignedUser;
    }
}
