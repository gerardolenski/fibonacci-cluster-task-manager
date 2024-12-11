package org.gol.taskmanager.task.infrastructure.outbox;

record TaskMessage(String taskId, String jobId, String data) {
}