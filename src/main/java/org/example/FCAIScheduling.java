package org.example;

import java.util.*;

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
        float lastArrivalTime = Float.MIN_VALUE;
        for (Process process : processes) {
            if (process.getArrivalTime() > lastArrivalTime) {
                lastArrivalTime = process.getArrivalTime();
            }
        }
        return lastArrivalTime / 10;
    }

    private float computeV2(List<Process> processes) {
        float maxBurstTime = Float.MIN_VALUE;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurstTime) {
                maxBurstTime = process.getBurstTime();
            }
        }
        return maxBurstTime / 10;
    }

    private float calculateFcaiFactor(Process process) {
        return (float) Math.ceil((10 - process.getPriority()) +
                (process.getArrivalTime() / V1) +
                (process.getBurstTime() / V2));
    }

    List<Process> get_ready_processes(List<Process> processes, int currentTime) {
        List<Process> ready_que = new ArrayList<>();
        for (Process process : processes) {
            if (process.getArrivalTime() > currentTime) {
                ready_que.add(process);
            }
        }
        ready_que.sort(Comparator.comparing(this::calculateFcaiFactor));
        return ready_que;
    }

    @Override
    public void detailedExecutionTimeline() {
        int currentTime = 0;
        List<Process> ready_que = get_ready_processes(processes, currentTime);
        while (ready_que.size() > 0) {
            Process running = ready_que.get(0);
            int executedTime = currentTime - running.getArrivalTime();
            if (running.getBurstTime() >= executedTime) {
                System.out.printf(running.getName(), "Completed");
                running.setBurstTime(0);
            }
            if (get_ready_processes(processes, currentTime + 1).get(0) == running) {

            }

            if (Math.ceil(running.getQuantum() * .4) < executedTime) {

            }
        }

    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        return processes;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        return processes;
    }

    @Override
    public void Display_initial_process_list() {
        System.out.printf("%-10s %-12s %-12s %-12s %-8s %-8s%n", "Processes", "Burst-time",
                "Arrival-time", "Priority", "Quantum", "Initial-FCAI-Factors");
        for (Process process : processes) {
            System.out.printf("%-10s %-12.1s %-12.1s %-12d %-8d %.2f%n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority(),
                    process.getQuantum(),
                    calculateFcaiFactor(process));
        }
    }

}
