package org.gol.taskmanager.domain.fib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.config.ConfigurationPort;
import org.gol.taskmanager.domain.manager.WorkerManagerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
class FibConfig {

    private final ConfigurationPort configurationPort;

    @Bean
    FibPort fibManager(WorkerManagerPort workerManagerPort) {
        var fibAlgorithms = configurationPort.getFibonacciAlgorithms().stream()
                .flatMap(configAlgorithm -> stream(FibAlgorithm.values())
                        .filter(fibAlgorithm -> fibAlgorithm.name().equals(configAlgorithm)))
                .collect(toSet());
        log.info("Activating Fibonacci numbers algorithms: {}", fibAlgorithms);
        return new FibManager(workerManagerPort, fibAlgorithms);
    }
}
