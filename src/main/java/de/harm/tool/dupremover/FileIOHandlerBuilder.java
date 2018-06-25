package de.harm.tool.dupremover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileIOHandlerBuilder {

  public IOHandler<String> build(final Path srcPath, final Path targetPath) throws IOException {
    log.info("Opening: in:{} and out:{}", srcPath, targetPath);
    InputStream fin = openInputStream(srcPath);
    BufferedReader bin = new BufferedReader(new InputStreamReader(fin, StandardCharsets.UTF_8));

    Supplier<String> src = () -> {
      try {
        return bin.readLine();
      } catch (IOException e) {
        throw new IllegalStateException("Unable to read line", e);
      }

    };
    OutputStream outs = openOutputStream(targetPath);
    PrintStream outw = new PrintStream(outs, true, StandardCharsets.UTF_8.toString());
    Consumer<String> target = outw::println;

    Consumer<IOHandler<String>> finnalizer = stringIOHandler -> {
      log.info("Closing: in:{} and out:{}", srcPath, targetPath);
      try {

        bin.close();
      } catch (IOException e) {
        log.warn("Unable to close src: {}", srcPath, e);
      }
      outw.close();
    };
    return new FunctionalHandler<>(src, target, finnalizer);
  }

  private boolean isGzipFile(Path srcPath) {
    return srcPath.getFileName().toString().endsWith(".gz");
  }

  private InputStream openInputStream(Path srcPath) throws IOException {
    boolean useGzip = isGzipFile(srcPath);

    InputStream fin = Files.newInputStream(srcPath, StandardOpenOption.READ);
    if (useGzip) {
      return new GZIPInputStream(fin);
    } else {
      return fin;
    }
  }

  private OutputStream openOutputStream(Path srcPath) throws IOException {
    boolean useGzip = isGzipFile(srcPath);

    OutputStream fout = Files.newOutputStream(srcPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    if (useGzip) {
      return new GZIPOutputStream(fout);
    }
    return fout;
  }
}
