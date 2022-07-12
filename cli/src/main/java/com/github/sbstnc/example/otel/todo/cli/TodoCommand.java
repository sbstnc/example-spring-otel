package com.github.sbstnc.example.otel.todo.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Slf4j
@Component
@CommandLine.Command(
    name = "todo",
    subcommands = {AddTodoCommand.class, UpdateTodoCommand.class, DeleteTodoCommand.class})
public class TodoCommand {}
