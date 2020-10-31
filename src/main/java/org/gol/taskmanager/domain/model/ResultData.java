package org.gol.taskmanager.domain.model;

import java.util.UUID;

public interface ResultData {
    UUID getTaskId();

    UUID getJobId();

    String getResult();

    Long getProcessingTime();

    String getErrorMessage();
}
