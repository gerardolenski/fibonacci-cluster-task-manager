package org.gol.taskmanager.task.domain.model.task;

import org.gol.taskmanager.task.domain.model.job.Jobs;

public interface Task {

    TaskId taskId();

    TaskType type();

    Jobs jobs();
}
