package de.harm.tool.dupremover;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DupRemoverApplication implements ApplicationRunner {

  private final SimpleTargetPathBuilder targetBuilder = new SimpleTargetPathBuilder(".filtered");
  @Autowired
  private FileIOHandlerBuilder fileIOHandlerBuilder;

  public static void main(String[] args) {
    SpringApplication.run(DupRemoverApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {

    List<IOHandler<String>> fileHandlers = args.getNonOptionArgs().stream()
        .map(this::buildFileHandler)
        .collect(Collectors.toList());

    DupRemover<String> proc = new DupRemover<>();
    proc.process(fileHandlers, Comparator.nullsFirst(Comparator.naturalOrder()));

  }

  private IOHandler<String> buildFileHandler(String srcFileName) {
    Path srcPath = Paths.get(srcFileName);
    Path targetPath = Paths.get(targetBuilder.targetPath(srcFileName));
    try {
      return fileIOHandlerBuilder.build(srcPath, targetPath);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to create handler for: " + srcFileName, e);
    }
  }
}
