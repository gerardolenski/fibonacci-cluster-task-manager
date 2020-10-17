package org.gol.taskmanager.domain.fib;

import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerManagerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class FibConfiguration {

    @Bean("fibManager")
    FibManager initFibManager(WorkerManagerPort workerManagerPort) {
        return new FibManager(workerManagerPort);
    }
}
