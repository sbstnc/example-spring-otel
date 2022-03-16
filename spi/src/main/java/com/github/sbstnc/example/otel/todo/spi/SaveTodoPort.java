package com.github.sbstnc.example.otel.todo.spi;

import com.github.sbstnc.example.otel.todo.domain.Todo;

public interface SaveTodoPort {
  Todo save(final Todo todo);
}
