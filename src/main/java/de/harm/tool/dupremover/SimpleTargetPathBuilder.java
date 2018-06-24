package de.harm.tool.dupremover;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;

@Data
public class SimpleTargetPathBuilder {

    private final String postfix;


    public String targetPath(String srcPath) {
        String extension = FilenameUtils.getExtension(srcPath);
        boolean keepExtension = "gz".equals(extension);

        String base = keepExtension ? FilenameUtils.removeExtension(srcPath) : srcPath;

        String target = base + postfix;
        if (keepExtension) {
            return target + '.' + extension;
        } else {
            return target;
        }
    }
}
