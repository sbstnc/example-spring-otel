package com.github.sbstnc.example.otel.todo.cli;

import com.github.sbstnc.example.otel.todo.api.UpdateTodoTitleCommand;
import com.github.sbstnc.example.otel.todo.api.UpdateTodoUseCase;
import com.github.sbstnc.example.otel.todo.domain.TodoStatus;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Component
@Command(name = "update", description = "Updates an existing todo.")
public class UpdateTodoCommand implements Callable<Integer> {
  @Option(
      names = {"-i", "--id"},
      description = "Identifier of the todo to update.")
  private UUID id;

  @Option(
      names = {"-t", "--title"},
      description = "The new title.")
  private Optional<String> title;

  @Option(
      names = {"-s", "--status"},
      description = "The new status.")
  private Optional<TodoStatus> status;

  private final UpdateTodoUseCase updateTodoUseCase;

  public UpdateTodoCommand(final UpdateTodoUseCase updateTodoUseCase) {
    this.updateTodoUseCase = updateTodoUseCase;
  }

  @Override
  public Integer call() throws Exception {
    title
        .flatMap(t -> updateTodoUseCase.updateTodoTitle(new UpdateTodoTitleCommand(this.id, t)))
        .ifPresent(System.out::println);
    status
        .flatMap(
            s -> {
              switch (s) {
                case PENDING:
                  return updateTodoUseCase.markTodoPending(this.id);
                case IN_PROGRESS:
                  return updateTodoUseCase.markTodoInProgress(this.id);
                case DONE:
                  return updateTodoUseCase.markTodoDone(this.id);
                default:
                  return Optional.empty();
              }
            })
        .ifPresent(System.out::println);
    return 0;
  }
}
