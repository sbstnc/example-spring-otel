package com.github.sbstnc.example.otel.todo.db;

import static com.github.sbstnc.example.otel.todo.db.Tables.TODO;

import com.github.sbstnc.example.otel.todo.db.tables.records.TodoRecord;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TodoRepository {
  private final DSLContext context;

  public int delete(final UUID id) {
    return context.deleteFrom(TODO).where(TODO.ID.eq(id)).execute();
  }

  public Optional<TodoRecord> findById(final UUID id) {
    return context.selectFrom(TODO).where(TODO.ID.eq(id)).fetchOptional();
  }

  public Stream<TodoRecord> findAll() {
    return context.selectFrom(TODO).fetchStream();
  }

  public TodoRecord save(final TodoRecord todoRecord) {
    if (todoRecord.configuration() == null) {
      todoRecord.attach(context.configuration());
    }
    todoRecord.store();
    return todoRecord;
  }
}
