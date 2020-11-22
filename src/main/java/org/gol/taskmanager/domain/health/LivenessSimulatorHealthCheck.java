package org.gol.taskmanager.domain.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static java.nio.file.Files.isRegularFile;
import static org.springframework.boot.actuate.health.Health.outOfService;
import static org.springframework.boot.actuate.health.Health.up;

/**
 * Only to simulate deadlock.
 */
@Slf4j
@Component("livenessSimulator")
public class LivenessSimulatorHealthCheck implements HealthIndicator {

    private static final Path DEADLOCK_PATH = Path.of("deadlock");

    @Override
    public Health health() {
        log.debug("Runs liveness health check");
        if (isRegularFile(DEADLOCK_PATH)) {
            return outOfService()
                    .withDetail("deadlock", "The application deadlock occurred.")
                    .build();
        }
        return up().build();
    }
}
