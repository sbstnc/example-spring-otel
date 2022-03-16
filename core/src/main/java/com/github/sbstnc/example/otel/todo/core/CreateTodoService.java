package com.github.sbstnc.example.otel.todo.core;

import com.github.sbstnc.example.otel.todo.api.CreateTodoCommand;
import com.github.sbstnc.example.otel.todo.api.CreateTodoUseCase;
import com.github.sbstnc.example.otel.todo.domain.Todo;
import com.github.sbstnc.example.otel.todo.domain.TodoStatus;
import com.github.sbstnc.example.otel.todo.spi.SaveTodoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTodoService implements CreateTodoUseCase {
  private final SaveTodoPort saveTodoPort;

  @Override
  public Todo createTodo(final CreateTodoCommand command) {
    final var todo =
        Todo.builder()
            .id(command.getId())
            .title(command.getTitle())
            .status(TodoStatus.PENDING)
            .build();
    return saveTodoPort.save(todo);
  }
}
