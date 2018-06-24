package de.harm.tool.dupremover;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class FileIOHandlerBuilder {

    public IOHandler<String> build(final Path srcPath, final Path targetPath) throws IOException {
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
