package com.github.sbstnc.example.otel.todo.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.github.sbstnc.example.otel.todo.domain.TodoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoResponseModelAssembler
    implements RepresentationModelAssembler<TodoResponse, EntityModel<TodoResponse>> {
  private final TodoStatusMapper statusMapper;

  @Override
  public EntityModel<TodoResponse> toModel(final TodoResponse response) {
    final var cls = TodoController.class;
    final var model =
        EntityModel.of(
            response,
            linkTo(methodOn(cls).getTodo(response.getId())).withSelfRel(),
            linkTo(methodOn(cls).getTodos()).withRel("todos"));

    final var status = statusMapper.map(response.getStatus());
    if (status != TodoStatus.PENDING) {
      model.add(linkTo(methodOn(cls).pending(response.getId())).withRel("setStatusPending"));
    }
    if (status != TodoStatus.IN_PROGRESS) {
      model.add(linkTo(methodOn(cls).inProgress(response.getId())).withRel("setStatusInProgress"));
    }
    if (status != TodoStatus.DONE) {
      model.add(linkTo(methodOn(cls).done(response.getId())).withRel("setStatusDone"));
    }

    return model;
  }
}
