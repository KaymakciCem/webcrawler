package com.ecoiso.webcrawler;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskManager {

    public void waitForTasks(List<Future<?>> tasks) {
        for (Future<?> task : tasks) {
            try {
                task.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task interrupted", e);
            } catch (ExecutionException e) {
                System.err.println("Task execution failed: " + e.getCause());
            }
        }
    }
}
