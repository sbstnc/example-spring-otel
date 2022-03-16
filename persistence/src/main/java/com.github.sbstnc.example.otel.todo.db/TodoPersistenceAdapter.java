package com.github.sbstnc.example.otel.todo.db;

import static java.util.stream.Collectors.toList;

import com.github.sbstnc.example.otel.todo.domain.Todo;
import com.github.sbstnc.example.otel.todo.spi.DeleteTodoPort;
import com.github.sbstnc.example.otel.todo.spi.LoadTodoPort;
import com.github.sbstnc.example.otel.todo.spi.SaveTodoPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class TodoPersistenceAdapter implements DeleteTodoPort, LoadTodoPort, SaveTodoPort {
  private final TodoRepository todoRepository;
  private final TodoRecordMapper todoRecordMapper;

  @Override
  public Todo save(final Todo todo) {
    final var record =
        todoRepository
            .findById(todo.getId())
            .map(todoRecordMapper.update(todo))
            .orElseGet(() -> todoRecordMapper.create(todo));
    log.info("Saving todo {}", record.getId());
    return todoRecordMapper.map(todoRepository.save(record));
  }

  @Override
  public Optional<Todo> findById(final UUID id) {
    return todoRepository.findById(id).map(todoRecordMapper::map);
  }

  @Override
  public List<Todo> findAll() {
    return todoRepository.findAll().map(todoRecordMapper::map).collect(toList());
  }

  @Override
  public int delete(final UUID id) {
    log.info("Deleting todo {} from repository", id);
    return todoRepository.delete(id);
  }
}
