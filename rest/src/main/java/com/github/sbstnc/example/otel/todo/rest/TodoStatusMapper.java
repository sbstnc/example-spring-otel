package com.github.sbstnc.example.otel.todo.rest;

import com.github.sbstnc.example.otel.todo.domain.TodoStatus;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class TodoStatusMapper {
  public TodoStatus map(final String status) {
    return TodoStatus.valueOf(status.replace(' ', '_').toUpperCase(Locale.US));
  }

  public String map(final TodoStatus status) {
    return status.name().toLowerCase(Locale.US).replace('_', ' ');
  }
}
