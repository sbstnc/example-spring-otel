package com.github.sbstnc.example.otel.todo.rest;

import java.util.UUID;
import lombok.Value;

@Value(staticConstructor = "of")
public class TodoResponse {
  UUID id;
  String title;
  String status;
}
