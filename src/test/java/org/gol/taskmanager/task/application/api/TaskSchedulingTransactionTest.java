package org.gol.taskmanager.task.application.api;


import org.gol.taskmanager.BaseIT;
import org.gol.taskmanager.outbox.infrastructure.db.OutboxRepositoryDelegate;
import org.gol.taskmanager.task.domain.JobNotificationPort;
import org.gol.taskmanager.task.domain.JobPersistCmd;
import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.gol.taskmanager.task.infrastructure.db.JobRepositoryDelegate;
import org.gol.taskmanager.task.support.TestTaskHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

class TaskSchedulingTransactionTest extends BaseIT {

    @Autowired
    private TaskManagementPort sut;
    @Autowired
    private OutboxRepositoryDelegate outboxRepository;
    @Autowired
    private JobRepositoryDelegate jobRepository;
    @SpyBean
    private JobRepositoryPort jobRepositoryPort;
    @SpyBean
    private JobNotificationPort jobNotificationPort;

    @BeforeEach
    void setUp() {
        outboxRepository.deleteAll();
        jobRepository.deleteAll();
    }

    @Test
    @DisplayName("should persist 3 jobs and messages [positive]")
    void schedulingWhenTransactionSucceed() {
        //given task with 3 jobs
        var taskHolder = new TestTaskHolder(3);

        //when
        sut.scheduleProcessing(taskHolder.task());

        //then jobs and outbox contains data
        assertThat(jobRepository.count()).isEqualTo(3);
        assertThat(outboxRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("should rollback everything when 3rd job notification failed [negative]")
    void schedulingWhenOutboxTransactionFailed() {
        //given task with 3 jobs and 3rd notification failure
        var taskHolder = new TestTaskHolder(3);
        doCallRealMethod()
                .doCallRealMethod()
                .doThrow(IllegalStateException.class)
                .when(jobNotificationPort)
                .notify(any(), any(), any());

        //when
        assertThatThrownBy(() -> sut.scheduleProcessing(taskHolder.task()))
                .isInstanceOf(IllegalStateException.class);

        //then everything was rollback
        assertThat(jobRepository.count()).isZero();
        assertThat(outboxRepository.count()).isZero();
    }

    @Test
    @DisplayName("should rollback everything when 3rd job failed [negative]")
    void schedulingWhenJobTransactionFailed() {
        //given task with 3 jobs and 3rd job persist failure
        var taskHolder = new TestTaskHolder(3);
        doCallRealMethod()
                .doCallRealMethod()
                .doThrow(IllegalStateException.class)
                .when(jobRepositoryPort)
                .persist(any(JobPersistCmd.class));

        //when
        assertThatThrownBy(() -> sut.scheduleProcessing(taskHolder.task()))
                .isInstanceOf(IllegalStateException.class);

        //then everything was rollback
        assertThat(jobRepository.count()).isZero();
        assertThat(outboxRepository.count()).isZero();
    }
}
