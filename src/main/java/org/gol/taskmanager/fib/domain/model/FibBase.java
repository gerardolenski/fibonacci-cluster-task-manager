package org.gol.taskmanager.fib.domain.model;

public record FibBase(int n) {

    public FibBase {
        if (n < 0)
            throw new IllegalArgumentException("Fibonacci base number must be natural number");
    }
}
