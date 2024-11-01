package org.gol.taskmanager.fib.infrastructure.task;

import org.gol.taskmanager.fib.domain.FibTaskSchedulingPort;
import org.gol.taskmanager.task.application.api.TaskManagementPort;

public class FibTaskSchedulerFactory {

    public FibTaskSchedulingPort fibTaskScheduler(TaskManagementPort taskManagementPort) {
        return new FibTaskScheduler(taskManagementPort);
    }
}
