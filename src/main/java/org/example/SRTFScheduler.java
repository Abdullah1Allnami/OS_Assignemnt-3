package org.example;

import java.util.List;

public class SRTFScheduler implements Scheduler {
    private List<Process> processes;

    SRTFScheduler(List<Process> processes) {
        this.processes = processes;
    }

    @Override
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
    @Override
    public void Display_initial_process_list() {

    }
}