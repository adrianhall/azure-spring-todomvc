package com.contoso.todomvc.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contoso.todomvc.models.ListFilter;
import com.contoso.todomvc.services.TodoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class TodoItemController {
    private final TodoService service;
    
    private static final String REDIRECT_TO_INDEX = "redirect:/";

    public TodoItemController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public String getAllItems(Model model) {
        return todomvcIndexPage(model, ListFilter.ALL);
    }

    @GetMapping("/active")
    public String getActiveItems(Model model) {
        return todomvcIndexPage(model, ListFilter.ACTIVE);

    }

    @GetMapping("/completed")
    public String getCompletedItems(Model model) {
        return todomvcIndexPage(model, ListFilter.COMPLETED);
    }

    @GetMapping("/learn.json")
    public ResponseEntity<Object> getLearnData() {
        Map<String, String> data = new HashMap<>();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private String todomvcIndexPage(Model model, ListFilter listFilter) {
        model.addAttribute("item", new TodoItemFormData());
        model.addAttribute("filter", listFilter);
        model.addAttribute("todos", service.getItems(listFilter));
        model.addAttribute("totalItemCount", service.countItemsWithState(ListFilter.ALL));
        model.addAttribute("activeItemCount", service.countItemsWithState(ListFilter.ACTIVE));
        model.addAttribute("completedItemCount", service.countItemsWithState(ListFilter.COMPLETED));
        return "index";
    }

    @PostMapping
    public String addNewItem(@Valid @ModelAttribute("item") TodoItemFormData formData) {
        service.createItem(formData.getTitle(), false);
        return REDIRECT_TO_INDEX;
    }

    @PutMapping("/{id}/toggle")
    public String toggleSelection(@PathVariable("id") Long id) {
        service.toggleCompletedById(id);
        return REDIRECT_TO_INDEX;
    }

    @PutMapping("/toggle-all")
    public String toggleAll() {
        service.toggleAllItems();
        return REDIRECT_TO_INDEX;
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        service.deleteById(id);
        return REDIRECT_TO_INDEX;
    }

    @DeleteMapping("/completed")
    public String deleteCompletedItems() {
        service.deleteCompletedItems();
        return REDIRECT_TO_INDEX;
    }
}
