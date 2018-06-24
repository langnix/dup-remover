package de.harm.tool.dupremover;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertThat;

public class FileIOHandlerBuilderTest {

    @Test
    public void runOnSingleFile() throws IOException {
        FileIOHandlerBuilder cut = new FileIOHandlerBuilder();
        Path srcPath = Paths.get("src/test/resources/src.1.txt");
        Path targetPath = Paths.get("target/out.1.txt");
        IOHandler<String> hd = cut.build(srcPath, targetPath);
        while (!hd.isEmpty()) {
            hd.use();
        }
        hd.finish();
        List<String> srcLines = Files.readAllLines(srcPath);
        List<String> targetLines = Files.readAllLines(targetPath);
        assertThat(targetLines, Matchers.is(srcLines));

    }

    @Test
    public void runWithSkip() throws IOException {
        FileIOHandlerBuilder cut = new FileIOHandlerBuilder();
        Path srcPath = Paths.get("src/test/resources/src.1.txt");
        Path targetPath = Paths.get("target/out.1.txt");
        IOHandler<String> hd = cut.build(srcPath, targetPath);
        while (!hd.isEmpty()) {
            if ("2".equals(hd.top())) {
                hd.skip();
            } else {
                hd.use();
            }
        }
        hd.finish();
        List<String> srcLines = Files.readAllLines(srcPath);
        srcLines.remove("2");
        List<String> targetLines = Files.readAllLines(targetPath);

        assertThat(targetLines, Matchers.is(srcLines));

    }


    @Test
    public void runWithGzipFiles() throws IOException {
        FileIOHandlerBuilder cut = new FileIOHandlerBuilder();
        Path srcPath = Paths.get("src/test/resources/src.1.txt");
        Path targetPath = Paths.get("target/out.1.gz");
        IOHandler<String> hd = cut.build(srcPath, targetPath);
        while (!hd.isEmpty()) {
            hd.use();
        }
        hd.finish();
        // and back
        Path target2Path = Paths.get("target/out.1.gz.txt");
        IOHandler<String> hd2 = cut.build(targetPath, target2Path);
        while (!hd2.isEmpty()) {
            hd2.use();
        }
        hd2.finish();
        List<String> srcLines = Files.readAllLines(srcPath);
        List<String> targetLines = Files.readAllLines(target2Path);
        assertThat(targetLines, Matchers.is(srcLines));

    }


}