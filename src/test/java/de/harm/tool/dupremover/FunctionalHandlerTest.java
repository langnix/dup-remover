package de.harm.tool.dupremover;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FunctionalHandlerTest {

    @Test
    public void vanilla() {
        Iterator<Integer> srcIt = Arrays.asList(1, 1, 3, 4).iterator();

        Supplier<Integer> src = () -> (srcIt.hasNext() ? srcIt.next() : null);
        List<Integer> res = new ArrayList<>();
        Consumer<Integer> target = res::add;
        FunctionalHandler<Integer> cut = new FunctionalHandler<>(src, target);
        assertThat(cut.top(), is(1));
        cut.use();
        assertThat(res.size(), is(1));
        assertThat(cut.top(), is(1));
        cut.skip();
        assertThat(res.size(), is(1));
        assertThat(cut.top(), is(3));

    }
}