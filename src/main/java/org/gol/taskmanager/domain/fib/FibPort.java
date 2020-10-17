package org.gol.taskmanager.domain.fib;

import java.util.List;
import java.util.UUID;

public interface FibPort {

    UUID calculateSeries(List<Integer> series);
}
