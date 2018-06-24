package de.harm.tool.dupremover;

import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
public class FunctionalHandler<T> implements IOHandler<T> {
    private final Supplier<T> src;
    private final Consumer<T> target;

    private final Consumer<IOHandler<T>> finisher;

    private T top;

    public FunctionalHandler(Supplier<T> src, Consumer<T> target) {
        this(src, target, (xx) -> {
        });
    }

    public FunctionalHandler(Supplier<T> src, Consumer<T> target, Consumer<IOHandler<T>> finisher) {
        this.src = src;
        this.target = target;
        this.finisher = finisher;
        top = src.get();

    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public T top() {
        return top;
    }

    @Override
    public void use() {
        target.accept(top);
        top = src.get();
    }

    @Override
    public void skip() {
        top = src.get();
    }

    @Override
    public void finish() {
        finisher.accept(this);
    }
}
