package com.github.sbstnc.example.otel.todo.spi;

import com.github.sbstnc.example.otel.todo.domain.Todo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadTodoPort {
  Optional<Todo> findById(final UUID id);

  List<Todo> findAll();
}
