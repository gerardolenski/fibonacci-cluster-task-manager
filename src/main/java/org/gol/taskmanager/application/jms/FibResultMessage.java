package org.gol.taskmanager.application.jms;

import org.gol.taskmanager.domain.model.ResultData;

import java.math.BigInteger;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import static java.util.Optional.ofNullable;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
class FibResultMessage implements ResultData {

    private final UUID taskId;
    private final UUID jobId;
    private final BigInteger result;
    private final Long processingTime;
    private final String errorMessage;

    @Override
    public String result() {
        return ofNullable(result)
                .map(BigInteger::toString)
                .orElse(null);
    }
}
