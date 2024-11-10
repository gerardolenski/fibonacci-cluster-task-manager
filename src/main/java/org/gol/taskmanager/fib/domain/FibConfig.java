package org.gol.taskmanager.fib.domain;

import org.gol.taskmanager.fib.domain.model.FibAlgorithm;
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
    FibCalculatorFactory fibManagerFactory(FibTaskSchedulingPort taskSchedulingPort, FibParams fibParams) {
        var fibAlgorithms = fibParams.algorithms().stream()
                .flatMap(configAlgorithm -> stream(FibAlgorithm.values())
                        .filter(fibAlgorithm -> fibAlgorithm.name().equals(configAlgorithm)))
                .collect(toSet());
        log.info("Initializing FibManagerFactory with configuration: algorithms={}", fibAlgorithms);
        return new FibCalculatorFactory(fibAlgorithms, taskSchedulingPort);
    }
}
