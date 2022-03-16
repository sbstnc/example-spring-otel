package com.github.sbstnc.example.otel.todo.domain;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class Todo {
  UUID id;
  Long version;
  String title;
  TodoStatus status;
  ZonedDateTime createdAt;
  ZonedDateTime updatedAt;

  public Todo setDone() {
    return this.toBuilder().status(TodoStatus.DONE).build();
  }

  public Todo setInProgress() {
    return this.toBuilder().status(TodoStatus.IN_PROGRESS).build();
  }

  public Todo setPending() {
    return this.toBuilder().status(TodoStatus.PENDING).build();
  }
}
