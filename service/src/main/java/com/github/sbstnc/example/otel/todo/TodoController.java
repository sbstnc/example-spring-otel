package com.github.sbstnc.example.otel.todo;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
  private final TodoService todoService;
  private final TodoResponseMapper responseMapper;
  private final TodoResponseModelAssembler assembler;

  @PostMapping
  public ResponseEntity<EntityModel<TodoResponse>> addTodo(@RequestBody TodoRequest request) {
    log.info("Creating new todo");
    final var todo = Todo.builder().title(request.getTitle()).build();
    final var em = assembler.toModel(responseMapper.map(todoService.saveTodo(todo)));
    return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<TodoResponse>>> getTodos() {
    log.info("Fetching all todos");
    final var todos =
        todoService.getAllTodos().stream()
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
    return todoService
        .getTodo(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<TodoResponse>> updateTodo(
      @RequestBody TodoRequest request, @PathVariable UUID id) {
    log.info("Updating todo {}", id);
    final var todo = Todo.builder().id(id).title(request.getTitle()).build();
    final var em = assembler.toModel(responseMapper.map(todoService.saveTodo(todo)));
    return ResponseEntity.created(em.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(em);
  }

  @PutMapping("/{id}/pending")
  public ResponseEntity<EntityModel<TodoResponse>> pending(@PathVariable UUID id) {
    log.info("Mark todo {} as pending", id);
    return todoService
        .setTodoStatusPending(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/in-progress")
  public ResponseEntity<EntityModel<TodoResponse>> inProgress(@PathVariable UUID id) {
    log.info("Mark todo {} as in progress", id);
    return todoService
        .setTodoStatusInProgress(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/done")
  public ResponseEntity<EntityModel<TodoResponse>> done(@PathVariable UUID id) {
    log.info("Mark todo {} as done", id);
    return todoService
        .setTodoStatusDone(id)
        .map(responseMapper::map)
        .map(assembler::toModel)
        .map(model -> ResponseEntity.ok().body(model))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
    todoService.deleteTodo(id);
    return ResponseEntity.noContent().build();
  }
}
