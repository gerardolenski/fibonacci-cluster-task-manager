package org.gol.taskmanager.infrastructure.jpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "task", indexes = {
        @Index(name = "task_task_id_start_time_idx", columnList = "task_id, start_time"),
        @Index(name = "task_job_id_key", columnList = "job_id", unique = true),
        @Index(name = "task_task_id_job_id_key", columnList = "task_id, job_id", unique = true)})
class TaskEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "job_details")
    private String jobDetails;

    @Column(name = "job_processing_time")
    private Long jobProcessingTime;

    @Column(name = "job_result")
    private String jobResult;
}
