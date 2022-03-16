package com.github.sbstnc.example.otel.todo.api;

import java.util.UUID;
import lombok.NonNull;
import lombok.Value;

@Value
public class UpdateTodoTitleCommand {
  @NonNull UUID id;
  @NonNull String title;
}
