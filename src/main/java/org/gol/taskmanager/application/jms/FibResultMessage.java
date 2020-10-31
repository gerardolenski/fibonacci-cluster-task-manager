package org.gol.taskmanager.application.jms;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.gol.taskmanager.domain.model.ResultData;

import java.math.BigInteger;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@ToString
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
class FibResultMessage implements ResultData {

    @Getter
    private final UUID taskId;
    @Getter
    private final UUID jobId;
    private final BigInteger result;
    @Getter
    private final Long processingTime;
    @Getter
    private final String errorMessage;

    @Override
    public String getResult() {
        return ofNullable(result)
                .map(BigInteger::toString)
                .orElse(null);
    }
}
