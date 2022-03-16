package com.github.sbstnc.example.otel.todo.core;

import com.github.sbstnc.example.otel.todo.api.UpdateTodoTitleCommand;
import com.github.sbstnc.example.otel.todo.api.UpdateTodoUseCase;
import com.github.sbstnc.example.otel.todo.domain.Todo;
import com.github.sbstnc.example.otel.todo.spi.LoadTodoPort;
import com.github.sbstnc.example.otel.todo.spi.SaveTodoPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateTodoService implements UpdateTodoUseCase {
  private final LoadTodoPort loadTodoPort;
  private final SaveTodoPort saveTodoPort;

  @Override
  public Optional<Todo> updateTodoTitle(final UpdateTodoTitleCommand command) {
    return loadTodoPort
        .findById(command.getId())
        .map(o -> o.toBuilder().title(command.getTitle()).build())
        .map(saveTodoPort::save);
  }

  @Override
  public Optional<Todo> markTodoPending(final UUID id) {
    return loadTodoPort.findById(id).map(Todo::setPending).map(saveTodoPort::save);
  }

  @Override
  public Optional<Todo> markTodoInProgress(final UUID id) {
    return loadTodoPort.findById(id).map(Todo::setInProgress).map(saveTodoPort::save);
  }

  @Override
  public Optional<Todo> markTodoDone(final UUID id) {
    return loadTodoPort.findById(id).map(Todo::setDone).map(saveTodoPort::save);
  }
}
