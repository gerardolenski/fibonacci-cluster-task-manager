package org.gol.taskmanager.domain.model;

import java.util.UUID;

public interface ResultData {

    UUID taskId();

    UUID jobId();

    String result();

    Long processingTime();

    String errorMessage();
}
