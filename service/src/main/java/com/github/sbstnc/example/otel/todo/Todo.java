package com.github.sbstnc.example.otel.todo;

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
}
