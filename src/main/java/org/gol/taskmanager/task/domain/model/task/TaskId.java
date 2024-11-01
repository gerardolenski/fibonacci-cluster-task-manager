package org.gol.taskmanager.task.domain.model.task;

import java.util.UUID;

import lombok.NonNull;

import static java.util.UUID.randomUUID;

public record TaskId(@NonNull UUID value) {

    public static TaskId generateTaskId() {
        return new TaskId(randomUUID());
    }
}
