package de.harm.tool.dupremover;

public interface IOHandler<T> {
    boolean isEmpty();

    T top();

    void use();

    void skip();


    void finish();
}
