package org.gol.taskmanager.domain.model;

import java.util.UUID;

public interface WorkerData {

    WorkerType workerType();

    UUID taskId();

    JobData jobData();
}
