package org.gol.taskmanager.task.infrastructure.db;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobRepositoryDelegate {

    private final JobRepository jobRepository;

    public void deleteAll() {
        jobRepository.deleteAll();
    }

    public long count() {
        return jobRepository.count();
    }
}
