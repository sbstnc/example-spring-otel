package com.github.sbstnc.example.otel.todo;

import static java.util.stream.Collectors.toList;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TodoService {
  private final TodoRepository todoRepository;
  private final TodoRecordMapper todoRecordMapper;

  public int deleteTodo(final UUID id) {
    log.info("Deleting todo {} from repository", id);
    return todoRepository.delete(id);
  }

  public Optional<Todo> getTodo(final UUID id) {
    log.info("Finding todo {} in repository", id);
    return todoRepository.findById(id).map(todoRecordMapper::map);
  }

  public List<Todo> getAllTodos() {
    log.info("Finding todos in repository");
    return todoRepository.findAll().map(todoRecordMapper::map).collect(toList());
  }

  public Optional<Todo> setTodoStatusPending(final UUID id) {
    return setTodoStatus(id, TodoStatus.PENDING);
  }

  public Optional<Todo> setTodoStatusInProgress(final UUID id) {
    return setTodoStatus(id, TodoStatus.IN_PROGRESS);
  }

  public Optional<Todo> setTodoStatusDone(final UUID id) {
    return setTodoStatus(id, TodoStatus.DONE);
  }

  @WithSpan("todo.save")
  public Todo saveTodo(final Todo todo) {
    final var record =
        todoRepository
            .findById(todo.getId())
            .map(todoRecordMapper.update(todo))
            .orElseGet(() -> todoRecordMapper.create(todo));
    Span.current().setAttribute("todo.save.id", record.getId().toString());
    log.info("Saving todo {}", record.getId());
    return todoRecordMapper.map(todoRepository.save(record));
  }

  private Optional<Todo> setTodoStatus(final UUID id, final TodoStatus status) {
    log.info("Setting status of todo {} to {}", id, status.name());
    return todoRepository
        .findById(id)
        .map(record -> record.setStatus(status.name()))
        .map(todoRepository::save)
        .map(todoRecordMapper::map);
  }
}
