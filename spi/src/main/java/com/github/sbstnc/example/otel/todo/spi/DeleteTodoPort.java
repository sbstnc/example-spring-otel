package com.github.sbstnc.example.otel.todo.spi;

import java.util.UUID;

public interface DeleteTodoPort {
  int delete(final UUID id);
}
