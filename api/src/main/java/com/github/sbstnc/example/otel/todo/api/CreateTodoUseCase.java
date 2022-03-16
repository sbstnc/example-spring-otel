package com.github.sbstnc.example.otel.todo.api;

import com.github.sbstnc.example.otel.todo.domain.Todo;

public interface CreateTodoUseCase {
  Todo createTodo(final CreateTodoCommand command);
}
