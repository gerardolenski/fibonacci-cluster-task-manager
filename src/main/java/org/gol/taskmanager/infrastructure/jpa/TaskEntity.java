package org.gol.taskmanager.infrastructure.jpa;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Task")
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

    @Column(name = "job_details", length = 1000)
    private String jobDetails;

    @Column(name = "job_processing_time")
    private Long jobProcessingTime;

    @Column(name = "job_result", columnDefinition = "TEXT")
    private String jobResult;
}
