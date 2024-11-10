package org.gol.taskmanager.fib.application.service;

import org.gol.taskmanager.fib.application.api.FibPort;
import org.gol.taskmanager.fib.domain.FibCalculatorFactory;
import org.gol.taskmanager.fib.domain.model.FibAlgorithm;
import org.gol.taskmanager.fib.infrastructure.task.FibTaskRetrieverFactory;
import org.gol.taskmanager.fib.infrastructure.task.FibTaskSchedulerFactory;
import org.gol.taskmanager.task.application.service.TaskModuleTestConfigurer;

import java.util.Set;

import lombok.Getter;
import lombok.experimental.Accessors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Getter
@Accessors(fluent = true)
public class FibModuleTestConfigurer {

    private final TaskModuleTestConfigurer taskModuleConfigurer;
    private final FibTaskSchedulerFactory fibTaskSchedulerFactory;
    private final FibTaskRetrieverFactory fibTaskRetrieverFactory;
    private final Set<FibAlgorithm> fibAlgorithms;
    private final FibPort fibPort;

    public FibModuleTestConfigurer(FibAlgorithm... algorithms) {
        taskModuleConfigurer = new TaskModuleTestConfigurer();
        fibTaskSchedulerFactory = new FibTaskSchedulerFactory();
        fibTaskRetrieverFactory = new FibTaskRetrieverFactory();
        fibAlgorithms = stream(algorithms).collect(toSet());

        var fibTaskScheduler = fibTaskSchedulerFactory.fibTaskScheduler(taskModuleConfigurer.taskManager());
        var fibTaskRetriever = fibTaskRetrieverFactory.fibTaskRetriever(taskModuleConfigurer.taskManager());
        var fibCalcFactory = new FibCalculatorFactory(stream(algorithms).collect(toSet()), fibTaskScheduler);
        fibPort = new FibService(fibCalcFactory, fibTaskRetriever);
    }
}
