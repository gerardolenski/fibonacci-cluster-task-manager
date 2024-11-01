package org.gol.taskmanager.task.infrastructure.db;

import org.gol.taskmanager.task.domain.model.task.TaskType;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Job")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "job", indexes = {
        @Index(name = "job_task_id_start_time_idx", columnList = "task_id, start_time"),
        @Index(name = "job_job_id_key", columnList = "job_id", unique = true),
        @Index(name = "job_task_id_job_id_key", columnList = "task_id, job_id", unique = true)})
class JobEntity {

    @EmbeddedId
    private JobKey id;

    @Enumerated(STRING)
    @Column(name = "type", length = 50)
    private TaskType type;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @Column(name = "job_processing_time")
    private Long jobProcessingTime;

    @Column(name = "job_details", columnDefinition = "TEXT")
    private String jobDetails;

    @Column(name = "job_result", columnDefinition = "TEXT")
    private String jobResult;
}
