package org.gol.taskmanager.task.infrastructure.db;


import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
class JobKey {

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;
}
