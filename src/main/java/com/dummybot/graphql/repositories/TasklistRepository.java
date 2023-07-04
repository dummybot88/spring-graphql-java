package com.dummybot.graphql.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasklistRepository extends CrudRepository<Tasklist, Long> {
    List<Tasklist> findAllByAssignedUser(String assignee);
}
