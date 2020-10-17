package org.gol.taskmanager.application.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
class FibRequest {
    private final List<Integer> series;
}
