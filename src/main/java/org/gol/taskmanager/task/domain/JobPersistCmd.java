package org.gol.taskmanager.task.domain;

import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import lombok.NonNull;

public record JobPersistCmd(@NonNull TaskId taskId,
                            @NonNull TaskType taskType,
                            @NonNull Job job) {
}
