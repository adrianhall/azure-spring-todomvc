package com.contoso.todomvc.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodoItemFormData {
    @NotBlank private String title;
}
