package com.github.sbstnc.example.otel.todo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TodoResponseModelAssembler
    implements RepresentationModelAssembler<TodoResponse, EntityModel<TodoResponse>> {
  @Override
  public EntityModel<TodoResponse> toModel(final TodoResponse response) {
    final var cls = TodoController.class;
    final var model =
        EntityModel.of(
            response,
            linkTo(methodOn(cls).getTodo(response.getId())).withSelfRel(),
            linkTo(methodOn(cls).getTodos()).withRel("todos"));

    if (response.getStatus() != TodoStatus.PENDING) {
      model.add(linkTo(methodOn(cls).pending(response.getId())).withRel("setStatusPending"));
    }
    if (response.getStatus() != TodoStatus.IN_PROGRESS) {
      model.add(linkTo(methodOn(cls).inProgress(response.getId())).withRel("setStatusInProgress"));
    }
    if (response.getStatus() != TodoStatus.DONE) {
      model.add(linkTo(methodOn(cls).done(response.getId())).withRel("setStatusDone"));
    }

    return model;
  }
}
