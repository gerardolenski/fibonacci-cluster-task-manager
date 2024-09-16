package org.gol.taskmanager.domain.fib;

import org.gol.taskmanager.domain.manager.WorkerManagerPort;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
class FibConfig {

    @ConfigurationProperties(prefix = "fib")
    record FibParams(Set<String> algorithms) {
    }

    @Bean
    FibPort fibManager(WorkerManagerPort workerManagerPort, FibParams fibParams) {
        var fibAlgorithms = fibParams.algorithms().stream()
                .flatMap(configAlgorithm -> stream(FibAlgorithm.values())
                        .filter(fibAlgorithm -> fibAlgorithm.name().equals(configAlgorithm)))
                .collect(toSet());
        log.info("Activating Fibonacci numbers algorithms: {}", fibAlgorithms);
        return new FibManager(workerManagerPort, fibAlgorithms);
    }
}
