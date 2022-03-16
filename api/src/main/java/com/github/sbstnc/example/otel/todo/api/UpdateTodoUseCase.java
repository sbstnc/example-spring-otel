package com.github.sbstnc.example.otel.todo.api;

import com.github.sbstnc.example.otel.todo.domain.Todo;
import java.util.Optional;
import java.util.UUID;

public interface UpdateTodoUseCase {
  Optional<Todo> updateTodoTitle(final UpdateTodoTitleCommand command);

  Optional<Todo> markTodoPending(final UUID id);

  Optional<Todo> markTodoInProgress(final UUID id);

  Optional<Todo> markTodoDone(final UUID id);
}
