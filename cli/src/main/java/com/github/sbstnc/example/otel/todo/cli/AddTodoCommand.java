package com.github.sbstnc.example.otel.todo.cli;

import com.github.sbstnc.example.otel.todo.api.CreateTodoCommand;
import com.github.sbstnc.example.otel.todo.api.CreateTodoUseCase;
import java.util.UUID;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Slf4j
@Component
@CommandLine.Command(name = "add", description = "Adds a new todo.")
public class AddTodoCommand implements Callable<Integer> {
  @CommandLine.Option(
      names = {"-t", "--title"},
      description = "The task at hand.")
  private String title;

  private final CreateTodoUseCase createTodoUseCase;

  public AddTodoCommand(final CreateTodoUseCase createTodoUseCase) {
    this.createTodoUseCase = createTodoUseCase;
  }

  @Override
  public Integer call() throws Exception {
    final var todo =
        createTodoUseCase.createTodo(new CreateTodoCommand(UUID.randomUUID(), this.title));
    System.out.println(todo);
    return 0;
  }
}
