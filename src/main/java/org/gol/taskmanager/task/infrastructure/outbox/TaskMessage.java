package org.gol.taskmanager.task.infrastructure.outbox;

import org.gol.taskmanager.task.domain.model.job.Job;

record TaskMessage(String taskId, Job data) {
}