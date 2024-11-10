package org.gol.taskmanager.task.application.service;

import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.support.TestJobResult;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskModuleSteps {

    private final TaskModuleTestConfigurer configurer;

    public List<JobId> taskJobs(TaskId taskId) {
        return configurer.taskRepository().taskJobs(taskId);
    }

    public void registerJobSuccessResult(TaskId taskId, JobId jobId, Long processingTime, String result) {
        var job1Result = TestJobResult.builder()
                .taskId(taskId)
                .jobId(jobId)
                .processingTime(processingTime)
                .result(result)
                .build();
        configurer.taskManager().registerResult(job1Result);
    }
}
