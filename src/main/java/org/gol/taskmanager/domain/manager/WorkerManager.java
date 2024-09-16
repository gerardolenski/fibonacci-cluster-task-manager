package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.WorkerData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class WorkerManager implements WorkerManagerPort {

    private final WorkerNotificationPort notificationPort;
    private final WorkerRepositoryPort repositoryPort;

    @Override
    public void processTask(WorkerData task) {
        log.debug("Starting task processing: {}", task);
        repositoryPort.persist(task);
        notificationPort.notify(task);
    }
}
