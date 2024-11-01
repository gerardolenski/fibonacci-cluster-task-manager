package org.gol.taskmanager.task.domain.model.job;

import java.util.UUID;

import lombok.NonNull;

import static java.util.UUID.randomUUID;

public record JobId(@NonNull UUID value) {

    public static JobId generateJobId() {
        return new JobId(randomUUID());
    }
}
