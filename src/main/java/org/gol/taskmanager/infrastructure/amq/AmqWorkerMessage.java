package org.gol.taskmanager.infrastructure.amq;

import org.gol.taskmanager.domain.model.JobData;

record AmqWorkerMessage(String taskId, JobData data) {
}
