package org.gol.taskmanager.fib.application.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.gol.taskmanager.fib.application.api.FibPort;
import org.gol.taskmanager.fib.domain.model.FibBase;
import org.gol.taskmanager.fib.domain.model.FibNumber;
import org.gol.taskmanager.fib.domain.model.FibSeriesCalculationCmd;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.application.service.TaskModuleSteps;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gol.taskmanager.fib.domain.model.FibAlgorithm.ITERATIVE;
import static org.gol.taskmanager.fib.domain.model.FibAlgorithm.RECURSIVE;

class FibServiceTest {

    private FibPort sut;
    private TaskManagementPort taskManager;
    private TaskModuleSteps taskModuleSteps;

    @BeforeEach
    void setUp() {
        var configurer = new FibModuleTestConfigurer(ITERATIVE, RECURSIVE);
        sut = configurer.fibPort();
        taskManager = configurer.taskModuleConfigurer().taskManager();
        taskModuleSteps = configurer.taskModuleConfigurer().steps();
    }

    @Test
    @DisplayName("the green flow of the fib module use cases [positive]")
    void greenFlow() {
        //given
        var cmd = new FibSeriesCalculationCmd(List.of(new FibBase(10), new FibBase(11), new FibBase(12)));

        //when request calculation
        var fibTaskId = sut.calculateSeries(cmd);


        //then
        var taskId = new TaskId(fibTaskId.value());
        var jobs = taskModuleSteps.taskJobs(taskId);
        assertThat(taskManager.checkStatistics(taskId))
                .satisfies(s -> assertThat(s.scheduledJobsCount()).isEqualTo(6))
                .satisfies(s -> assertThat(s.finishedJobsCount()).isZero());

        //when first job is finished with success
        var job1 = jobs.getFirst();
        taskModuleSteps.registerJobSuccessResult(taskId, job1, 100L, "55");

        //then fib result should contain it
        assertThat(sut.checkResult(fibTaskId))
                .satisfies(r -> assertThat(r.scheduledJobsCount()).isEqualTo(6))
                .satisfies(r -> assertThat(r.finishedJobsCount()).isOne())
                .satisfies(r -> assertThat(r.successesCount()).isOne())
                .satisfies(r -> assertThat(r.failuresCount()).isZero())
                .extracting(r -> r.successes().getFirst())
                .satisfies(r -> assertThat(r.number()).isEqualTo(new FibNumber(BigInteger.valueOf(55))));

        //when second job is finished with failure
        var job2 = jobs.get(1);
        taskModuleSteps.registerJobSuccessResult(taskId, job2, 100L, "error");

        //then fib result should contain it
        assertThat(sut.checkResult(fibTaskId))
                .satisfies(r -> assertThat(r.scheduledJobsCount()).isEqualTo(6))
                .satisfies(r -> assertThat(r.finishedJobsCount()).isEqualTo(2))
                .satisfies(r -> assertThat(r.successesCount()).isOne())
                .satisfies(r -> assertThat(r.failuresCount()).isOne())
                .extracting(r -> r.failures().getFirst())
                .satisfies(r -> assertThat(r.errorMessage()).isEqualTo("error"));

        //when remaining jobs finish with success
        jobs.stream()
                .skip(2)
                .forEach(j -> taskModuleSteps.registerJobSuccessResult(taskId, j, 100L, RandomStringUtils.insecure().nextNumeric(30)));

        //then fib result should contain it
        assertThat(sut.checkResult(fibTaskId))
                .satisfies(r -> assertThat(r.scheduledJobsCount()).isEqualTo(6))
                .satisfies(r -> assertThat(r.finishedJobsCount()).isEqualTo(6))
                .satisfies(r -> assertThat(r.successesCount()).isEqualTo(5))
                .satisfies(r -> assertThat(r.failuresCount()).isOne());
    }
}