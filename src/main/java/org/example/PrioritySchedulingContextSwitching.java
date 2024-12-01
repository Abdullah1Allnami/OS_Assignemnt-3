package org.example;

import java.util.List;

public class PrioritySchedulingContextSwitching implements Scheduler {
    private List<Process> processes;

    public void schedule() {
        // Implement the code here
    }

    @Override
    public void detailedExecutionTimeline() {
        // Implement the code here
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        // Implement the code here
        return List.of();
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        // Implement the code here
        return List.of();
    }

    public void DetailedExecutionTimeline() {
        // Implement the code here
    }

    PrioritySchedulingContextSwitching(List<Process> processes) {
        // Implement the code here
        this.processes = processes;
    }
    @Override
    public void Display_initial_process_list() {

    }
}