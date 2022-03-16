package com.github.sbstnc.example.otel.todo.core;

import com.github.sbstnc.example.otel.todo.api.GetTodoByIdQuery;
import com.github.sbstnc.example.otel.todo.api.GetTodoUseCase;
import com.github.sbstnc.example.otel.todo.domain.Todo;
import com.github.sbstnc.example.otel.todo.spi.LoadTodoPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTodoService implements GetTodoUseCase {
  private final LoadTodoPort loadTodoPort;

  @Override
  public Optional<Todo> findById(final GetTodoByIdQuery query) {
    return loadTodoPort.findById(query.getId());
  }

  @Override
  public List<Todo> findAll() {
    return loadTodoPort.findAll();
  }
}
