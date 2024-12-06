package org.example;

import java.util.List;

public class SRTFScheduler implements Scheduler {
    private List<Process> processes;

    // Aging parameters
    private final float contextSwitchTime = 0.5f;  // Time required for context switching
    private final int agingThreshold = 10;        // Threshold to start aging
    private final float scalingFactor = 5.0f;     // Factor to scale aging adjustments

    public SRTFScheduler(List<Process> processes) {
        this.processes = processes;
    }

    @Override
    public void detailedExecutionTimeline() {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int[] remainingBurstTime = new int[n];
        boolean[] isCompleted = new boolean[n];
        int[] waitingTime = new int[n]; // Track waiting times for aging
        Process previousProcess = null; // Track the previously running process

        // Initialize remaining burst times
        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processes.get(i).getBurstTime();
        }

        System.out.println("\nDetailed Execution Timeline:");
        while (completed < n) {
            int minIndex = -1;
            int minTime = Integer.MAX_VALUE;

            // Find the process with the shortest remaining burst time, considering aging
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && processes.get(i).getArrivalTime() <= currentTime) {
                    int effectiveBurstTime = remainingBurstTime[i];

                    // Apply aging if the process has waited too long
                    if (waitingTime[i] >= agingThreshold) {
                        effectiveBurstTime -= (waitingTime[i] / scalingFactor);
                        if (effectiveBurstTime < 1) effectiveBurstTime = 1; // Ensure minimum burst time
                    }

                    if (effectiveBurstTime < minTime) {
                        minTime = effectiveBurstTime;
                        minIndex = i;
                    }
                }
            }

            // If no process is ready, increment time
            if (minIndex == -1) {
                currentTime++;
                continue;
            }

            Process currentProcess = processes.get(minIndex);

            // Simulate context switching if the process is different from the previous one
            if (previousProcess != null && !previousProcess.getName().equals(currentProcess.getName())) {
                System.out.printf("Time %.1f: Context switch from %s to %s%n",
                        (float) currentTime,
                        previousProcess.getName(),
                        currentProcess.getName());
                currentTime += contextSwitchTime; // Add context switch time
            }

            System.out.printf("Time %d: Process %s starts execution%n", currentTime, currentProcess.getName());

            // Execute the process for one time unit
            remainingBurstTime[minIndex]--;
            currentTime++;

            // Update waiting times for all other processes
            for (int i = 0; i < n; i++) {
                if (i != minIndex && !isCompleted[i] && processes.get(i).getArrivalTime() <= currentTime) {
                    waitingTime[i]++;
                }
            }

            // Check if process is completed
            if (remainingBurstTime[minIndex] == 0) {
                isCompleted[minIndex] = true;
                completed++;
                int completionTime = currentTime;
                currentProcess.setTurnaroundTime(completionTime - currentProcess.getArrivalTime());
                float waitTime = currentProcess.getTurnaroundTime() - currentProcess.getBurstTime();
                currentProcess.setWaitTime(waitTime);
                System.out.printf("Time %d: Process %s completes execution%n", currentTime, currentProcess.getName());
            }

            // Update the previous process
            previousProcess = currentProcess;
        }
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        System.out.println("\nWaiting Times:");
        for (Process process : processes) {
            System.out.printf("Process %s waiting time: %.1f%n", process.getName(), process.getWaitTime());
        }
        return processes;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        System.out.println("\nTurnaround Times:");
        for (Process process : processes) {
            System.out.printf("Process %s turnaround time: %.1f%n", process.getName(), process.getTurnaroundTime());
        }
        return processes;
    }

    @Override
    public void Display_initial_process_list() {
        System.out.printf("%-10s %-12s %-12s %-12s %n", "Processes", "Burst-time", "Arrival-time", "Priority");
        for (Process process : processes) {
            System.out.printf("%-10s %-12d %-12d %-12d %n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority());
        }
    }
}
