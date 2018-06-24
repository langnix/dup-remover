package de.harm.tool.dupremover;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;

public class ListIOHanlderBuilderTest {

    @Test
    public void vanila() {
        ListIOHanlderBuilder cut = new ListIOHanlderBuilder();
        List<Integer> res = new ArrayList<>();

        IOHandler<Integer> intHd = cut.build(Arrays.asList(1, 2, 3), res);
        assertThat(intHd.top(), Matchers.is(1));
        assertThat(intHd.isEmpty(), Matchers.is(false));
        intHd.use();
        assertThat(intHd.top(), Matchers.is(2));
        assertThat(intHd.isEmpty(), Matchers.is(false));
        intHd.skip();
        assertThat(intHd.top(), Matchers.is(3));
        assertThat(intHd.isEmpty(), Matchers.is(false));
        intHd.use();
        assertThat(intHd.isEmpty(), Matchers.is(true));
        assertThat(res.size(), Matchers.is(2));
        assertThat(res, Matchers.is(Arrays.asList(1, 3)));


    }
}