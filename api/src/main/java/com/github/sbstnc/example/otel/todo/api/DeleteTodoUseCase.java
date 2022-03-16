package com.github.sbstnc.example.otel.todo.api;

import java.util.UUID;

public interface DeleteTodoUseCase {
  boolean deleteTodo(final UUID id);
}
