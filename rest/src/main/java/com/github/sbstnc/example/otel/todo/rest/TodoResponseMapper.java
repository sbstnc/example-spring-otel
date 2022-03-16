package com.github.sbstnc.example.otel.todo.rest;

import com.github.sbstnc.example.otel.todo.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoResponseMapper {
  private final TodoStatusMapper statusMapper;

  public TodoResponse map(final Todo todo) {
    return TodoResponse.of(todo.getId(), todo.getTitle(), statusMapper.map(todo.getStatus()));
  }
}
