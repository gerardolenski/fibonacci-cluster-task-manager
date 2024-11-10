package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.JobDetails;

public record TestJobDetails(String value) implements JobDetails {
    @Override
    public String serializedFormat() {
        return value;
    }
}
