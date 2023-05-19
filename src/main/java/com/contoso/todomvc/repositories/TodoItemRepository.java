package com.contoso.todomvc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contoso.todomvc.models.TodoItem;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    int countAllByCompleted(boolean completed);
    List<TodoItem> findAllByCompleted(boolean completed);
}
