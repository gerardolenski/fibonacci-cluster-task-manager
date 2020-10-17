package org.gol.taskmanager.infrastructure.amq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.gol.taskmanager.domain.model.JobData;

@Getter
@ToString
@RequiredArgsConstructor
class AmqWorkerMessage {
    private final String taskId;
    private final JobData data;
}
