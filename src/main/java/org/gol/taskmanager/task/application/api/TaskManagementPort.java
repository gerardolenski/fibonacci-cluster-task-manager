package org.gol.taskmanager.task.application.api;

import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;


/**
 * Primary port to implement task module use cases.
 */
public interface TaskManagementPort {

    /**
     * Registers new task. It means that all composed jobs should be processed by specific worker.
     */
    void scheduleProcessing(Task task);


    /**
     * Registers result of processed job.
     */
    void registerResult(JobResult result);

    /**
     * Retrieves the processing statistics of the given task.
     */
    TaskStats checkStatistics(TaskId taskId);
}
