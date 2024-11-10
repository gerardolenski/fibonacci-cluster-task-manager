package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.job.JobId;

public record TestJob(JobId jobId, TestJobDetails jobDetails) implements Job {
}
