package com.github.sbstnc.example.otel.todo;

import org.springframework.stereotype.Component;

@Component
public class TodoResponseMapper {
  public TodoResponse map(final Todo todo) {
    return new TodoResponse(todo.getId(), todo.getTitle(), todo.getStatus());
  }
}
