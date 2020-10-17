package org.gol.taskmanager.domain.model;

public interface WorkerMessage {
    String getTaskId();
    WorkerType getWorkerType();
}
