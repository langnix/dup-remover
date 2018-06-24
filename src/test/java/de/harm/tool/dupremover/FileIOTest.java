package de.harm.tool.dupremover;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileIOTest {
    @Test
    public void nioInstream() throws IOException {

        InputStream fin = Files.newInputStream(Paths.get("src/test/resources/src.1.txt"), StandardOpenOption.READ);
        BufferedReader bin = new BufferedReader(new InputStreamReader(fin, StandardCharsets.UTF_8));
        bin.readLine();
    }
}
