package org.gol.taskmanager.fib.application.adapter.rest;

import org.gol.taskmanager.fib.domain.model.FibTaskId;

record FibTaskResponse(String taskId) {

    static FibTaskResponse ofTaskId(FibTaskId taskId) {
        return new FibTaskResponse(taskId.value().toString());
    }
}
