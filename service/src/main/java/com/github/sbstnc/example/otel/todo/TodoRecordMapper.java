package com.github.sbstnc.example.otel.todo;

import com.github.sbstnc.example.otel.db.tables.records.TodoRecord;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.function.Function;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

@Component
public class TodoRecordMapper implements RecordMapper<TodoRecord, Todo> {
  public Todo map(final TodoRecord record) {
    return Todo.builder()
        .id(record.getId())
        .version(record.getVersion())
        .title(record.getTitle())
        .status(TodoStatus.valueOf(record.getStatus()))
        .createdAt(record.getCreatedAt().atZoneSameInstant(ZoneOffset.UTC))
        .updatedAt(record.getUpdatedAt().atZoneSameInstant(ZoneOffset.UTC))
        .build();
  }

  public TodoRecord create(final Todo todo) {
    return new TodoRecord()
        .setId(todo.getId() == null ? UUID.randomUUID() : todo.getId())
        .setTitle(todo.getTitle())
        .setStatus(TodoStatus.PENDING.name())
        .setCreatedAt(OffsetDateTime.now());
  }

  public Function<TodoRecord, TodoRecord> update(final Todo todo) {
    return persisted -> {
      final var persistedId = persisted.getId();
      final var incomingId = todo.getId();
      if (!persistedId.equals(incomingId)) {
        final var err =
            String.format("Incoming id %s does not match persisted id %s", incomingId, persistedId);
        throw new IllegalArgumentException(err);
      }
      return persisted.setTitle(todo.getTitle());
    };
  }
}
