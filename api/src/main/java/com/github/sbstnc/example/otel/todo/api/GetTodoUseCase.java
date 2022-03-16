package com.github.sbstnc.example.otel.todo.api;

import com.github.sbstnc.example.otel.todo.domain.Todo;
import java.util.List;
import java.util.Optional;

public interface GetTodoUseCase {
  Optional<Todo> findById(final GetTodoByIdQuery query);

  List<Todo> findAll();
}
