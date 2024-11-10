package org.gol.taskmanager.fib.application.adapter.rest;

import org.gol.taskmanager.BaseIT;
import org.gol.taskmanager.fib.application.api.FibPort;
import org.gol.taskmanager.fib.domain.model.FibNumber;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibFailureResult;
import org.gol.taskmanager.fib.domain.model.result.FibJobId;
import org.gol.taskmanager.fib.domain.model.result.FibSuccessResult;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.readString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class FibRestAdapterTest extends BaseIT {

    private static final FibTaskId TASK_ID = new FibTaskId(UUID.fromString("f804a4bc-f533-4aa0-8eca-b0fc25ebc3f8"));
    private static final FibJobId JOB_ID_1 = new FibJobId(UUID.fromString("fa2b46f9-f274-4a10-9181-44a8c95591ea"));
    private static final FibJobId JOB_ID_2 = new FibJobId(UUID.fromString("8b49b8ec-c6f7-48fc-8dda-2483e4ffdba7"));
    private static final FibJobId JOB_ID_3 = new FibJobId(UUID.fromString("ee0a5ea1-a525-4749-ae38-9430ec1fd2ab"));
    private static final Path CONTRACT_DIR = Path.of("src/test/resources/fib/rest-contract");

    @MockBean
    private FibPort fibService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("should accept Fibonacci series calculation task [positive]")
    void name() throws Exception {
        //given
        var requestBody = readString(CONTRACT_DIR.resolve("fib-request.json"));
        doReturn(TASK_ID).when(fibService).calculateSeries(any());

        //when, then
        var response = mvc.perform(post("/fib")
                        .header(ACCEPT, APPLICATION_JSON_VALUE)
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isAccepted())
                .andReturn();
        JSONAssert.assertEquals(readString(CONTRACT_DIR.resolve("task-response-202.json")), response.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should respond with task result [positive]")
    void checkResult() throws Exception {
        //given
        var job1 = FibSuccessResult.builder()
                .jobId(JOB_ID_1)
                .startTime(LocalDateTime.parse("2024-11-04T22:45:40.112"))
                .stopTime(LocalDateTime.parse("2024-11-04T22:45:42.512"))
                .jobDescription("FIBONACCI(10) -> RECURSIVE")
                .number(new FibNumber(BigInteger.valueOf(55)))
                .jobProcessingTime(2400L)
                .build();
        var job2 = FibSuccessResult.builder()
                .jobId(JOB_ID_2)
                .startTime(LocalDateTime.parse("2024-11-04T22:45:49.437"))
                .stopTime(LocalDateTime.parse("2024-11-04T22:45:49.442"))
                .jobDescription("FIBONACCI(10) -> ITERATIVE")
                .number(new FibNumber(BigInteger.valueOf(55)))
                .jobProcessingTime(5L)
                .build();

        var job3 = FibFailureResult.builder()
                .jobId(JOB_ID_3)
                .startTime(LocalDateTime.parse("2024-11-04T22:45:50.437"))
                .stopTime(LocalDateTime.parse("2024-11-04T22:45:50.438"))
                .jobDescription("FIBONACCI(100) -> RECURSIVE")
                .errorMessage("Number is too big for recursive algorithm")
                .jobProcessingTime(1L)
                .build();
        var taskResult = FibTaskResult.builder()
                .taskId(TASK_ID)
                .scheduledJobsCount(6)
                .successes(List.of(job1, job2))
                .failures(List.of(job3))
                .build();
        doReturn(taskResult)
                .when(fibService)
                .checkResult(TASK_ID);

        //when, then
        var response = mvc.perform(get("/fib/" + TASK_ID.value())
                        .header(ACCEPT, APPLICATION_JSON_VALUE))
                .andExpect(status().isMultiStatus())
                .andReturn();
        JSONAssert.assertEquals(readString(CONTRACT_DIR.resolve("task-result-response-206.json")), response.getResponse().getContentAsString(), false);
    }
}