package org.gol.taskmanager.domain.model;

import java.util.UUID;

public interface JobData {
    UUID getJobId();
    String getJobDetails();
}
