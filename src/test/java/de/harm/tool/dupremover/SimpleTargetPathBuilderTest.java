package de.harm.tool.dupremover;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class SimpleTargetPathBuilderTest {

    @Test
    public void plain() {
        SimpleTargetPathBuilder cut = new SimpleTargetPathBuilder(".filtered");

        assertThat(cut.targetPath("some/path.txt"), Matchers.is("some/path.txt.filtered"));
        assertThat(cut.targetPath("some/path.txt.gz"), Matchers.is("some/path.txt.filtered.gz"));
    }
}