package org.example;

import java.util.List;

public class FCAIScheduling implements Scheduler {
    private static float V1;
    private static float V2;
    private List<Process> processes;

    FCAIScheduling(List<Process> processes) {
        this.processes = processes;
        V1 = computeV1(processes);
        V2 = computeV2(processes);
    }

    private float computeV1(List<Process> processes) {
        // Implement
        return 0;
    }

    private float computeV2(List<Process> processes) {
        // Implement
        return 0;
    }

    private float calculateFcaiFactor(Process process) {
        // Implement
        return 0;
    }

    @Override
    public void schedule() {
        // Implement
    }

    @Override
    public void detailedExecutionTimeline() {
        // Implement
        return;
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        return List.of();
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        return List.of();
    }

    @Override
    public void Display_initial_process_list() {

    }
}
