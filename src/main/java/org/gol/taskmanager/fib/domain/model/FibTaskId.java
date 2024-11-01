package org.gol.taskmanager.fib.domain.model;

import java.util.UUID;

import lombok.NonNull;

public record FibTaskId(@NonNull UUID value) {
}
