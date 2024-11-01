package org.gol.taskmanager.task.domain.model.task;

import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobStats;

import java.util.List;
import java.util.Optional;

public record TaskStats(long scheduledJobsCount, List<JobStats> finishedJobs) {

    public long finishedJobsCount() {
        return finishedJobs.size();
    }

    public Optional<JobStats> jobStats(JobId jobId) {
        return finishedJobs.stream()
                .filter(j -> j.jobId().equals(jobId))
                .findFirst();
    }
}
