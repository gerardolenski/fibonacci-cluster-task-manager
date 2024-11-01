package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.job.Jobs;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.experimental.Accessors;

import static org.gol.taskmanager.task.domain.model.job.JobId.generateJobId;
import static org.gol.taskmanager.task.domain.model.task.TaskId.generateTaskId;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;

@Getter
@Accessors(fluent = true)
public class TestTaskHolder {

    private final TaskId taskId;
    private final List<Job> jobs;
    private final Task task;

    public TestTaskHolder(int jobsCount) {
        this.taskId = generateTaskId();
        this.jobs = Stream.generate(() -> new TestJob(generateJobId(), new TestJobDetails(RandomStringUtils.randomAlphanumeric(20))))
                .limit(jobsCount)
                .map(Job.class::cast)
                .toList();
        this.task = new TestTask(taskId, FIBONACCI, new Jobs(this.jobs));
    }

    public Job job(int ndx) {
        return jobs.get(ndx);
    }
}
