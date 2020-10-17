package org.gol.taskmanager.application.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
class FibResponse {
    private final String taskId;

    static FibResponse ofFibResponse(UUID taskId) {
        return new FibResponse(taskId.toString());
    }
}
