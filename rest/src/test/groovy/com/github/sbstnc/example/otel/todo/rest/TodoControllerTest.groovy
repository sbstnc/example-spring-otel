package com.github.sbstnc.example.otel.todo.rest

import com.github.sbstnc.example.otel.todo.api.CreateTodoCommand
import com.github.sbstnc.example.otel.todo.api.CreateTodoUseCase
import com.github.sbstnc.example.otel.todo.api.UpdateTodoUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.EntityModel
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.RequestEntity.delete
import static org.springframework.http.RequestEntity.get
import static org.springframework.http.RequestEntity.post
import static org.springframework.http.RequestEntity.put

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class TodoControllerTest extends Specification {
    static ENTITY_MODEL_TODO_RESPONSE =
            new ParameterizedTypeReference<EntityModel<TodoResponse>>() {}

    @Autowired
    TestRestTemplate rest

    @Autowired
    CreateTodoUseCase createTodo

    @Autowired
    UpdateTodoUseCase updateTodo

    static UUID_REGEX =
            /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/

    def "can create a new todo with a random identifier"() {
        given:
        def body = new TodoRequest()
        body.title = "title"

        when:
        def response = rest.exchange(
                post("/api/todos").body(body), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body.content.id ==~ UUID_REGEX
        response.body.content.title == "title"
        response.body.content.status == "pending"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusInProgress")
        response.body.hasLink("setStatusDone")
    }

    def "can create a new todo with a given identifier"() {
        given:
        def body = new TodoRequest()
        body.title = "title"
        def id = UUID.fromString("b20759b5-4d9e-4403-be23-92b6d60f1fa4")

        when:
        def response = rest.exchange(
                put("/api/todos/$id").body(body), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body.content.id == id
        response.body.content.title == "title"
        response.body.content.status == "pending"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusInProgress")
        response.body.hasLink("setStatusDone")
    }

    def "can get an existing todo"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "title"))

        when:
        def response = rest.exchange(
                get("/api/todos/$id").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.id == id
        response.body.content.title == "title"
        response.body.content.status == "pending"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusInProgress")
        response.body.hasLink("setStatusDone")
    }

    def "trying to get a non existent todo returns a not found status code"() {
        given:
        def id = UUID.randomUUID()

        when:
        def response = rest.exchange(
                get("/api/todos/$id").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
        !response.hasBody()
    }

    def "can update the title of an existing todo"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "initial title"))

        when:
        def body = new TodoRequest()
        body.setTitle("new title")
        def response = rest.exchange(
                put("/api/todos/$id").body(body), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.title == "new title"
    }

    def "can mark an existing todo as pending"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "title"))
        updateTodo.markTodoInProgress(id)

        when:
        def response = rest.exchange(
                put("/api/todos/$id/pending").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.id == id
        response.body.content.title == "title"
        response.body.content.status == "pending"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusInProgress")
        response.body.hasLink("setStatusDone")
    }

    def "can mark an existing todo as in progress"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "title"))

        when:
        def response = rest.exchange(
                put("/api/todos/$id/in-progress").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.id == id
        response.body.content.title == "title"
        response.body.content.status == "in progress"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusPending")
        response.body.hasLink("setStatusDone")
    }

    def "can mark an existing todo as done"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "title"))

        when:
        def response = rest.exchange(
                put("/api/todos/$id/done").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.OK
        response.body.content.id == id
        response.body.content.title == "title"
        response.body.content.status == "done"
        response.body.hasLink("self")
        response.body.hasLink("todos")
        response.body.hasLink("setStatusPending")
        response.body.hasLink("setStatusInProgress")
    }

    def "trying to mark a non existent todo as #status returns a not found status code"() {
        given:
        def id = UUID.randomUUID()

        when:
        def response = rest.exchange(
                put("/api/todos/$id/$status").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
        !response.hasBody()

        where:
        status << ["pending", "in-progress", "done"]
    }

    def "can delete an existing todo"() {
        given:
        def id = UUID.randomUUID()
        createTodo.createTodo(new CreateTodoCommand(id, "title"))

        when:
        def response = rest.exchange(
                delete("/api/todos/$id").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
        !response.hasBody()
    }

    def "can delete an non existent todo"() {
        given:
        def id = UUID.randomUUID()

        when:
        def response = rest.exchange(
                delete("/api/todos/$id").build(), ENTITY_MODEL_TODO_RESPONSE)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
        !response.hasBody()
    }
}
