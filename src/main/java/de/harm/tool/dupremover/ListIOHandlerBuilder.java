package de.harm.tool.dupremover;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;

public class ListIOHandlerBuilder {

  public <T> ListIOHandler<T> build(final List<T> srcList, final List<T> targetList) {
    return new ListIOHandler<>(new ListSupplier<>(srcList), targetList);
  }

  @Getter
  public static class ListIOHandler<T> extends FunctionalHandler<T> {

    final List<T> targetList;
    final ListSupplier<T> src;

    protected ListIOHandler(final ListSupplier<T> src, final List<T> targetList) {
      super(src, targetList::add);
      this.targetList = targetList;
      this.src = src;
    }
  }

  public static class ListSupplier<T> implements Supplier<T> {

    @Getter
    final List<T> list;
    Iterator<T> it;

    protected ListSupplier(List<T> list) {
      this.list = list;
      reset();
    }

    @Override
    public T get() {
      return it.hasNext() ? it.next() : null;
    }

    private void reset() {
      this.it = list.iterator();
    }
  }
}
