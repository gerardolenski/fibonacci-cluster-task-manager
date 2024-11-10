package org.gol.taskmanager.task.application.adapter.rest;

import org.gol.taskmanager.BaseIT;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;
import org.gol.taskmanager.task.support.TestJobStats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.readString;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@AutoConfigureMockMvc
class TaskRestAdapterTest extends BaseIT {

    private static final long SCHEDULED_JOBS_COUNT = 5L;
    private static final TaskId TASK_ID = new TaskId(UUID.fromString("f804a4bc-f533-4aa0-8eca-b0fc25ebc3f8"));
    private static final JobId JOB_ID_1 = new JobId(UUID.fromString("fa2b46f9-f274-4a10-9181-44a8c95591ea"));
    private static final JobId JOB_ID_2 = new JobId(UUID.fromString("8b49b8ec-c6f7-48fc-8dda-2483e4ffdba7"));
    private static final Path CONTRACT_DIR = Path.of("src/test/resources/task/rest-contract");

    @MockBean
    private TaskManagementPort taskManagementPort;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("should respond correctly for valid request [positive]")
    void response200() throws Exception {
        //given
        var job1Stats = TestJobStats.builder()
                .taskId(TASK_ID)
                .taskType(FIBONACCI)
                .jobId(JOB_ID_1)
                .startTime(LocalDateTime.parse("2024-11-04T22:45:40.112"))
                .stopTime(LocalDateTime.parse("2024-11-04T22:45:42.512"))
                .jobDetails("FIBONACCI(10) -> RECURSIVE")
                .jobResult("55")
                .jobProcessingTime(2400L)
                .build();
        var job2Stats = TestJobStats.builder()
                .taskId(TASK_ID)
                .taskType(FIBONACCI)
                .jobId(JOB_ID_2)
                .startTime(LocalDateTime.parse("2024-11-04T22:45:49.437"))
                .stopTime(LocalDateTime.parse("2024-11-04T22:45:49.442"))
                .jobDetails("FIBONACCI(10) -> ITERATIVE")
                .jobResult("55")
                .jobProcessingTime(5L)
                .build();
        doReturn(new TaskStats(SCHEDULED_JOBS_COUNT, List.of(job1Stats, job2Stats)))
                .when(taskManagementPort)
                .checkStatistics(TASK_ID);

        //when, then
        var result = mvc.perform(MockMvcRequestBuilders.get("/task/" + TASK_ID.value())
                        .header(ACCEPT, APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var responseBody = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(readString(CONTRACT_DIR.resolve("task-response-200.json")), responseBody, false);
    }
}