package com.github.sbstnc.example.otel.todo.cli;

import com.github.sbstnc.example.otel.todo.api.DeleteTodoUseCase;
import java.util.UUID;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Component
@Command(name = "delete", description = "Deletes an existing todo.")
public class DeleteTodoCommand implements Callable<Integer> {
  @Option(
      names = {"-i", "--id"},
      description = "Identifier of the todo to update.")
  private UUID id;

  private final DeleteTodoUseCase deleteTodoUseCase;

  public DeleteTodoCommand(final DeleteTodoUseCase deleteTodoUseCase) {
    this.deleteTodoUseCase = deleteTodoUseCase;
  }

  @Override
  public Integer call() throws Exception {
    if (deleteTodoUseCase.deleteTodo(id)) {
      System.out.println("Deleted todo with ID " + id);
    }
    return 0;
  }
}
