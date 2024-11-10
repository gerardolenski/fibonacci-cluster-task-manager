package org.gol.taskmanager.task.application.adapter.rest;

import org.gol.taskmanager.task.domain.model.job.JobStats;

import java.util.List;

import lombok.Builder;

@Builder
record TaskResponse(String taskId,
                    long scheduledJobsCount,
                    long finishedJobsCount,
                    List<JobResponse> finishedJobs) {

    @Builder
    record JobResponse(String jobId,
                       String startTime,
                       String stopTime,
                       String jobDetails,
                       String jobResult,
                       Long jobProcessingTime) {
        static JobResponse fromStats(JobStats stats) {
            return JobResponse.builder()
                    .jobId(stats.jobId().value().toString())
                    .startTime(stats.startTime().toString())
                    .stopTime(stats.stopTime().toString())
                    .jobDetails(stats.jobDetails())
                    .jobResult(stats.jobResult())
                    .jobProcessingTime(stats.jobProcessingTime())
                    .build();
        }
    }
}
