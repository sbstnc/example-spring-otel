package com.github.sbstnc.example.otel.todo.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.github.sbstnc.example.otel.todo.api.CreateTodoCommand;
import com.github.sbstnc.example.otel.todo.api.CreateTodoUseCase;
import com.github.sbstnc.example.otel.todo.api.DeleteTodoUseCase;
import com.github.sbstnc.example.otel.todo.api.GetTodoByIdQuery;
import com.github.sbstnc.example.otel.todo.api.GetTodoUseCase;
import com.github.sbstnc.example.otel.todo.api.UpdateTodoTitleCommand;
import com.github.sbstnc.example.otel.todo.api.UpdateTodoUseCase;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TodoController {
  private final CreateTodoUseCase createTodoUseCase;
  private final GetTodoUseCase getTodoUseCase;
  private final UpdateTodoUseCase updateTodoUseCase;
  private final DeleteTodoUseCase deleteTodoUseCase;
  private final TodoResponseMapper responseMapper;
  private final TodoResponseModelAssembler assembler;

  @PostMapping
  public ResponseEntity<EntityModel<TodoResponse>> addTodo(@RequestBody TodoRequest request) {
    log.info("Creating new todo");
    var todo =
        createTodoUseCase.createTodo(new CreateTodoCommand(UUID.randomUUID(), request.getTitle()));
    final var em = assembler.toModel(responseMapper.map(todo));
    return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<TodoResponse>>> getTodos() {
    log.info("Fetching all todos");
    final var todos =
        getTodoUseCase.findAll().stream()
            .map(responseMapper::map)
            .map(assembler::toModel)
            .collect(toList());
    final var cm =
        CollectionModel.of(todos, linkTo(methodOn(TodoController.class).getTodos()).withSelfRel());
    return ResponseEntity.ok().body(cm);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<TodoResponse>> getTodo(@PathVariable UUID id) {
    log.info("Fetching todo {}", id);
    return getTodoUseCase
        .findById(new GetTodoByIdQuery(id))
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<TodoResponse>> updateTodo(
      @RequestBody TodoRequest request, @PathVariable UUID id) {
    log.info("Updating todo {}", id);
    final var title = request.getTitle();
    final var todo =
        updateTodoUseCase
            .updateTodoTitle(new UpdateTodoTitleCommand(id, title))
            .orElseGet(() -> createTodoUseCase.createTodo(new CreateTodoCommand(id, title)));
    final var em = assembler.toModel(responseMapper.map(todo));
    return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
  }

  @PutMapping("/{id}/pending")
  public ResponseEntity<EntityModel<TodoResponse>> pending(@PathVariable UUID id) {
    log.info("Mark todo {} as pending", id);
    return updateTodoUseCase
        .markTodoPending(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/in-progress")
  public ResponseEntity<EntityModel<TodoResponse>> inProgress(@PathVariable UUID id) {
    log.info("Mark todo {} as in progress", id);
    return updateTodoUseCase
        .markTodoInProgress(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/done")
  public ResponseEntity<EntityModel<TodoResponse>> done(@PathVariable UUID id) {
    log.info("Mark todo {} as done", id);
    return updateTodoUseCase
        .markTodoDone(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteTodo(@PathVariable UUID id) {
    deleteTodoUseCase.deleteTodo(id);
    return ResponseEntity.noContent().build();
  }
}
