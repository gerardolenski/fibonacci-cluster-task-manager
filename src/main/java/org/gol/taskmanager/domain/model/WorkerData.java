package org.gol.taskmanager.domain.model;

import java.util.UUID;

public interface WorkerData {

    WorkerType getWorkerType();

    UUID getTaskId();

    JobData getJobData();
}
