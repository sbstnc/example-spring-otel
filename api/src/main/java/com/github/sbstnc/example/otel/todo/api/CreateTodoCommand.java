package com.github.sbstnc.example.otel.todo.api;

import java.util.UUID;
import lombok.NonNull;
import lombok.Value;

@Value
public class CreateTodoCommand {
  @NonNull UUID id;
  @NonNull String title;
}
