package com.github.sbstnc.example.otel.todo.core;

import com.github.sbstnc.example.otel.todo.api.DeleteTodoUseCase;
import com.github.sbstnc.example.otel.todo.spi.DeleteTodoPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTodoService implements DeleteTodoUseCase {
  private final DeleteTodoPort deleteTodoPort;

  @Override
  public boolean deleteTodo(final UUID id) {
    return deleteTodoPort.delete(id) > 0;
  }
}
