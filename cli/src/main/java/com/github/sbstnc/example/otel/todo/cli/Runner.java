package com.github.sbstnc.example.otel.todo.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner, ExitCodeGenerator {
  private final TodoCommand todoCommand;
  private final CommandLine.IFactory commandLineFactory;
  private int exitCode = 0;

  @Override
  public void run(String... args) {
    exitCode = new CommandLine(todoCommand, commandLineFactory).execute(args);
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }
}
