package org.gol.taskmanager.application.rest;

import java.util.UUID;

record FibResponse(String taskId) {

    static FibResponse ofTaskId(UUID taskId) {
        return new FibResponse(taskId.toString());
    }
}
