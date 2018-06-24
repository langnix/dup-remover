package de.harm.tool.dupremover;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListIOHanlderBuilder {

    public <T> IOHandler<T> build(final List<T> srcList, final List<T> targetList) {
        Iterator<T> srcIt = srcList.iterator();

        Supplier<T> src = () -> (srcIt.hasNext() ? srcIt.next() : null);

        Consumer<T> target = targetList::add;
        return new FunctionalHandler<>(src, target);
    }
}
