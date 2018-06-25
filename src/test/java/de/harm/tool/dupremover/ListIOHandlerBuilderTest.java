package de.harm.tool.dupremover;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.harm.tool.dupremover.ListIOHandlerBuilder.ListIOHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class ListIOHandlerBuilderTest {

  @Test
  public void vanila() {
    ListIOHandlerBuilder cut = new ListIOHandlerBuilder();
    List<Integer> res = new ArrayList<>();

    IOHandler<Integer> intHd = cut.build(Arrays.asList(1, 2, 3), res);
    assertThat(intHd.top(), is(1));
    assertThat(intHd.isEmpty(), is(false));
    intHd.use();
    assertThat(intHd.top(), is(2));
    assertThat(intHd.isEmpty(), is(false));
    intHd.skip();
    assertThat(intHd.top(), is(3));
    assertThat(intHd.isEmpty(), is(false));
    intHd.use();
    assertThat(intHd.isEmpty(), is(true));
    assertThat(res.size(), is(2));
    assertThat(res, is(Arrays.asList(1, 3)));


  }

  @Test
  public void withEmpty() {
    ListIOHandlerBuilder cut = new ListIOHandlerBuilder();
    ListIOHandler<Object> hd = cut.build(Collections.emptyList(), new ArrayList<>());
    assertThat(hd.isEmpty(), is(true));
    assertThat(hd.getTargetList().isEmpty(), is(true));

  }
}