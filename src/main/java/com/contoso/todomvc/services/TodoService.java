package com.contoso.todomvc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.contoso.todomvc.exceptions.TodoItemNotFoundException;
import com.contoso.todomvc.models.ListFilter;
import com.contoso.todomvc.models.TodoItem;
import com.contoso.todomvc.repositories.TodoItemRepository;

@Service
public class TodoService {
    private final TodoItemRepository repository;
    private Log logger = LogFactory.getLog(TodoService.class);

    public TodoService(TodoItemRepository repository) {
        this.repository = repository;
    }

    public int countItemsWithState(ListFilter listFilter) {
        logger.debug(String.format("countItemsWithState %s", listFilter));
        return switch (listFilter) {
            case ALL -> (int)repository.count();
            case ACTIVE -> repository.countAllByCompleted(false);
            case COMPLETED -> repository.countAllByCompleted(true);
        };
    }

    public void createItem(String title, boolean completed) {
        logger.debug(String.format("createItem %s %s", title, completed));
        TodoItem item = new TodoItem(title, completed);
        repository.save(item);
    }

    public void deleteById(Long id) {
        logger.debug(String.format("deleteById %s", id));
        repository.deleteById(id);
    }

    public void deleteCompletedItems() {
        logger.debug("deleteCompletedItems()");
        List<TodoItem> items = repository.findAllByCompleted(true);
        for (TodoItem item : items) {
            repository.deleteById(item.getId());
        }
    }

    public List<TodoItemDto> getItems(ListFilter listFilter) {
        logger.debug(String.format("getItems %s", listFilter));
        List<TodoItem> items = switch (listFilter) {
            case ALL -> repository.findAll();
            case ACTIVE -> repository.findAllByCompleted(false);
            case COMPLETED -> repository.findAllByCompleted(true);
        };
        return items.stream().map(TodoItemDto::new).collect(Collectors.toList());
    }

    public void toggleAllItems() {
        logger.debug("toggleAllItems()");
        List<TodoItem> items = repository.findAll();
        for (TodoItem item : items) {
            item.setCompleted(!item.isCompleted());
            repository.save(item);
        }
    }

    public void toggleCompletedById(Long id) {
        logger.debug(String.format("toggleCompletedById %s", id));
        TodoItem item = repository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
        item.setCompleted((!item.isCompleted()));
        repository.save(item);
    }

    public static record TodoItemDto(long id, String title, boolean completed) {
        public TodoItemDto(TodoItem item) {
            this(item.getId(), item.getTitle(), item.isCompleted());
        }
    }
}
