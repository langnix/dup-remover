package de.harm.tool.dupremover;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import de.harm.tool.dupremover.ListIOHandlerBuilder.ListIOHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;

public class DupRemoverTest {

  private DupRemover<Integer> cut = new DupRemover<>();

  private ListIOHandlerBuilder builder = new ListIOHandlerBuilder();

  @Test
  public void singleHandler() {
    List<Integer> src = Arrays.asList(1, 2, 3);
    List<Integer> target = new ArrayList<>();
    IOHandler<Integer> h1 = builder.build(src, target);
    List<IOHandler<Integer>> handlers = Collections.singletonList(h1);

    cut.process(handlers, Comparator.nullsFirst(Comparator.naturalOrder()));
    assertThat(target, is(src));
  }

  @Test
  public void multiples() {
    ListIOHandler<Integer> h1 = builder.build(Arrays.asList(1, 3, 5), new ArrayList<>());
    ListIOHandler<Integer> hEmpty = builder.build(Collections.emptyList(), new ArrayList<>());
    ListIOHandler<Integer> hSomeDup = builder.build(Arrays.asList(1, 2, 3, 4, 5), new ArrayList<>());
    ListIOHandler<Integer> hOneUniqe = builder.build(Collections.singletonList(6), new ArrayList<>());

    cut.process(Arrays.asList(h1, hEmpty, hSomeDup, hOneUniqe), Comparator.nullsFirst(Comparator.naturalOrder()));
    // no ordering, but just content
    Set<Integer> uniqOnes = new HashSet<>();
    Stream.of(h1, hEmpty, hSomeDup, hOneUniqe)
        .map(ListIOHandler::getTargetList)
        .forEach(tl -> tl.forEach(iVal -> {
              assertThat("no duplicates", uniqOnes.add(iVal), is(true));
            })
        );
    assertThat(uniqOnes.size(), is(6));
    assertThat(uniqOnes, contains(1, 2, 3, 4, 5, 6));

    Stream.of(h1, hEmpty, hSomeDup, hOneUniqe)
        .forEach(hd -> assertThat(hd.getTargetList().size(), is(lessThanOrEqualTo(hd.getSrc().getList().size()))));

    assertThat(hEmpty.getTargetList(), is(empty()));
    assertThat(hOneUniqe.getTargetList(), is(Collections.singletonList(6)));


  }
}