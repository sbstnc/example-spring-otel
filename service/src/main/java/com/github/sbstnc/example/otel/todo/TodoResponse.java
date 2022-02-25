package com.github.sbstnc.example.otel.todo;

import java.util.UUID;
import lombok.Value;

@Value
public class TodoResponse {
  UUID id;
  String title;
  TodoStatus status;
}
