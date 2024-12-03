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
        float last_arrival_time = Float.MIN_VALUE;
        for (Process process : processes) {
            if (process.getArrivalTime() > last_arrival_time) {
                last_arrival_time = process.getArrivalTime();
            }
        }
        return last_arrival_time / 10;
    }

    private float computeV2(List<Process> processes) {
        float max_brust_time = Float.MIN_VALUE;
        for (Process process : processes) {
            if (process.getBurstTime() > max_brust_time) {
                max_brust_time = process.getBurstTime();
            }
        }
        return max_brust_time / 10;
    }

    private float calculateFcaiFactor(Process process) {
        return (float) Math.ceil((10 - process.getPriority()) +
                (process.getArrivalTime() / V1) +
                (process.getBurstTime() / V2));
    }

    @Override
    public void schedule() {
        System.out.printf("%-10s %-12s %-12s %-12s %-8s %-8s%n", "Processes", "Burst-time", "Arrival-time", "Priority", "Quantum", "Initial-FCAI-Factors");
        for (Process process : processes) {
            System.out.printf("%-10s %-12.1f %-12.1f %-12d %-8d %-8s %n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority(),
                    process.getQuantum(),
                    calculateFcaiFactor(process)
            );
        }
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
        System.out.printf("%-10s %-12s %-12s %-12s %-8s%n", "Processes", "Burst-time", "Arrival-time", "Priority", "Quantum");
        for (Process process : processes) {
            System.out.printf("%-10s %-12.1f %-12.1f %-12d %-8d%n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority(),
                    process.getQuantum());
        }


    }
}
