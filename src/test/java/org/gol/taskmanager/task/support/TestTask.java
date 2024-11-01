package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.Jobs;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

public record TestTask(TaskId taskId, TaskType type, Jobs jobs) implements Task {
}
