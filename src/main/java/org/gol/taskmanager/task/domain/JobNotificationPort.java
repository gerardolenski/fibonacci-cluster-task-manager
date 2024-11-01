package org.gol.taskmanager.task.domain;

import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

public interface JobNotificationPort {

    void notify(TaskId taskId, TaskType taskType, Job job);
}
