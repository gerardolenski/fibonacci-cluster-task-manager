package org.gol.taskmanager.fib.infrastructure.task;

import org.gol.taskmanager.fib.domain.FibTaskResultPort;
import org.gol.taskmanager.task.application.api.TaskManagementPort;

public class FibTaskRetrieverFactory {

    public FibTaskResultPort fibTaskRetriever(TaskManagementPort taskManagementPort) {
        return new FibTaskResultRetriever(taskManagementPort);
    }
}
