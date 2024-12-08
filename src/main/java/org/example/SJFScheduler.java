package org.example;

import java.util.*;

public class SJFScheduler implements Scheduler {

    private List<Process> processes;
    private List<Process> completedProcesses;
    private int currentTime;

    SJFScheduler(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.completedProcesses = new ArrayList<>();
        this.currentTime = 0;
    }

    @Override
    public void detailedExecutionTimeline() {
        List<Process> remainingProcesses = new ArrayList<>(processes);
        currentTime = 0;
        completedProcesses.clear();

        System.out.println("\nDetailed Execution Timeline:");
        System.out.println("Time\tProcess");

        while (!remainingProcesses.isEmpty()) {
            Process nextProcess = getNextProcess(remainingProcesses, currentTime);

            if (nextProcess != null) {
                // Set process start time if it hasn't started yet
                if (nextProcess.getStartTime() == 0) {
                    nextProcess.setStartTime(currentTime);
                }

                // Execute the process
                System.out.println(currentTime + "\t" + nextProcess.getName() + " starts");
                currentTime += nextProcess.getBurstTime();
                System.out.println(currentTime + "\t" + nextProcess.getName() + " completes");

                // Calculate turnaround time and waiting time
                float turnaroundTime = currentTime - nextProcess.getArrivalTime();
                float waitingTime = turnaroundTime - nextProcess.getBurstTime();

                nextProcess.setTurnaroundTime(turnaroundTime);
                nextProcess.setWaitTime(waitingTime);

                remainingProcesses.remove(nextProcess);
                completedProcesses.add(nextProcess);
            } else {
                // No process available at current time, move to next arrival time
                int nextArrivalTime = Integer.MAX_VALUE;
                for (Process p : remainingProcesses) {
                    if (p.getArrivalTime() > currentTime && p.getArrivalTime() < nextArrivalTime) {
                        nextArrivalTime = p.getArrivalTime();
                    }
                }
                currentTime = nextArrivalTime;
            }
        }
    }

    private Process getNextProcess(List<Process> remainingProcesses, int currentTime) {
        Process selectedProcess = null;
        int shortestBurst = Integer.MAX_VALUE;

        for (Process process : remainingProcesses) {
            if (process.getArrivalTime() <= currentTime && process.getBurstTime() < shortestBurst) {
                shortestBurst = process.getBurstTime();
                selectedProcess = process;
            }
        }

        return selectedProcess;
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        if (completedProcesses.isEmpty()) {
            detailedExecutionTimeline();
        }
        return completedProcesses;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        if (completedProcesses.isEmpty()) {
            detailedExecutionTimeline();
        }
        return completedProcesses;
    }

    @Override
    public void Display_initial_process_list() {
        System.out.println("\nInitial Process List:");
        System.out.println("Process\tArrival Time\tBurst Time\tPriority\tQuantum");
        for (Process p : processes) {
            System.out.println(p.getName() + "\t" + p.getArrivalTime() + "\t\t" +
                    p.getBurstTime() + "\t\t" + p.getPriority() + "\t\t" + p.getQuantum());
        }
    }
}