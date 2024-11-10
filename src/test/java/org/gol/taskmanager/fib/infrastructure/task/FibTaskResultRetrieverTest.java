package org.gol.taskmanager.fib.infrastructure.task;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.gol.taskmanager.fib.domain.model.FibNumber;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibSuccessResult;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.model.job.JobStats;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;
import org.gol.taskmanager.task.support.TestJobStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gol.taskmanager.task.domain.model.job.JobId.generateJobId;
import static org.gol.taskmanager.task.domain.model.task.TaskId.generateTaskId;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FibTaskResultRetrieverTest {

    @Mock
    private TaskManagementPort taskManager;
    private FibTaskResultRetriever sut;

    @BeforeEach
    void setUp() {
        sut = new FibTaskResultRetriever(taskManager);
    }

    @Test
    @DisplayName("should correctly retrieve entire jobs collection [positive]")
    void shouldRetrieveCorrectly() {
        //given
        var taskId = generateTaskId();
        var taskStats = new TaskStats(3, List.of(
                generateJobStats(taskId, "55"),
                generateJobStats(taskId, "15"),
                generateJobStats(taskId, "fatal error")));
        doReturn(taskStats).when(taskManager).checkStatistics(any());

        //when
        var res = sut.checkResult(new FibTaskId(taskId.value()));

        //then
        assertThat(res.scheduledJobsCount()).isEqualTo(3);
        assertThat(res.successesCount()).isEqualTo(2);
        assertThat(res.failuresCount()).isEqualTo(1);
        assertThat(res.successes().stream().map(FibSuccessResult::number))
                .containsExactlyInAnyOrder(new FibNumber(BigInteger.valueOf(55)), new FibNumber(BigInteger.valueOf(15)));
        assertThat(res.failures())
                .allSatisfy(r -> assertThat(r.errorMessage()).isEqualTo("fatal error"));
    }

    JobStats generateJobStats(TaskId taskId, String result) {
        return TestJobStats.builder()
                .taskId(taskId)
                .jobId(generateJobId())
                .taskType(FIBONACCI)
                .jobDetails(RandomStringUtils.insecure().nextAlphabetic(10))
                .jobResult(result)
                .startTime(now())
                .stopTime(now().plusNanos(RandomUtils.insecure().randomInt(10, 100000)))
                .jobProcessingTime(RandomUtils.insecure().randomLong(10, 1000))
                .build();
    }
}