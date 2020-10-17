package org.gol.taskmanager.domain.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.model.WorkerData;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.*;

@Slf4j
@RequiredArgsConstructor
class WorkerManager implements WorkerManagerPort {

    private final WorkerNotificationPort notificationPort;
    private final WorkerRepositoryPort repositoryPort;

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public void processTask(WorkerData task) {
        log.debug("Starting task processing: {}", task);
        repositoryPort.persist(task);
        notificationPort.notify(task);
    }
}
