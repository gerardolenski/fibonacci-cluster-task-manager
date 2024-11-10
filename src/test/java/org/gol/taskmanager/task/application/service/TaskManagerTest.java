package org.gol.taskmanager.task.application.service;

import org.gol.taskmanager.outbox.application.service.OutboxModuleSteps;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.support.TestTaskHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;

class TaskManagerTest {

    private TaskModuleTestConfigurer testConfig;
    private TaskManagementPort sut;
    private TaskModuleSteps steps;
    private OutboxModuleSteps outboxModuleSteps;

    @BeforeEach
    void setUp() {
        testConfig = new TaskModuleTestConfigurer();
        sut = testConfig.taskManager();
        steps = testConfig.steps();
        outboxModuleSteps = testConfig.outboxConfig().steps();
    }

    @Test
    @DisplayName("the green flow of the task module use cases [positive]")
    void greenFlow() {
        //given
        var taskHolder = new TestTaskHolder(3);
        var taskId = taskHolder.taskId();
        var task = taskHolder.task();
        var job1 = taskHolder.job(0);
        var job2 = taskHolder.job(1);
        var job3 = taskHolder.job(2);

        //when register new task
        sut.scheduleProcessing(task);

        //then stats contains scheduled count but not finished
        assertThat(sut.checkStatistics(taskId))
                .satisfies(s -> assertThat(s.scheduledJobsCount()).isEqualTo(3))
                .satisfies(s -> assertThat(s.finishedJobsCount()).isZero());

        //then message relay can find all new jobs
        assertThat(outboxModuleSteps.outboxMessagesCount()).isEqualTo(3);

        //when register first job result
        steps.registerJobSuccessResult(taskId, job1.jobId(), 100L, "55");

        //then stats should contain finished job
        var stats = sut.checkStatistics(task.taskId());
        assertThat(stats)
                .satisfies(s -> assertThat(s.scheduledJobsCount()).isEqualTo(3))
                .satisfies(s -> assertThat(s.finishedJobsCount()).isEqualTo(1));
        assertThat(stats.jobStats(job1.jobId()))
                .isNotEmpty()
                .hasValueSatisfying(j -> assertThat(j.taskId()).isEqualTo(taskId))
                .hasValueSatisfying(j -> assertThat(j.taskType()).isEqualTo(FIBONACCI))
                .hasValueSatisfying(j -> assertThat(j.jobId()).isEqualTo(job1.jobId()))
                .hasValueSatisfying(j -> assertThat(Duration.between(j.startTime(), j.stopTime()).toMillis()).isGreaterThanOrEqualTo(0))
                .hasValueSatisfying(j -> assertThat(j.jobResult()).isEqualTo("55"))
                .hasValueSatisfying(j -> assertThat(j.jobDetails()).isEqualTo(job1.jobDetails().serializedFormat()));

        //when register remaining job result
        steps.registerJobSuccessResult(taskId, job2.jobId(), 200L, "89");
        steps.registerJobSuccessResult(taskId, job3.jobId(), 300L, "144");

        //then stats should contain finished job
        stats = sut.checkStatistics(task.taskId());
        assertThat(stats)
                .satisfies(s -> assertThat(s.scheduledJobsCount()).isEqualTo(3))
                .satisfies(s -> assertThat(s.finishedJobsCount()).isEqualTo(3));
    }
}