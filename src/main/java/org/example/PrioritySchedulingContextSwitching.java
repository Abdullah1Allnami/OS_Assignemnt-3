package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrioritySchedulingContextSwitching implements Scheduler {
    private List<Process> processes;
    private List<Process> completedProcesses;

    public PrioritySchedulingContextSwitching(List<Process> processes) {
        this.processes = new ArrayList<>();
        for (Process p : processes) {
            this.processes.add(new Process(p.getName(), p.getArrivalTime(), p.getBurstTime(), p.getPriority(), p.getQuantum()));
        }
        this.completedProcesses = new ArrayList<>();
        processExecution();
    }

    private void processExecution() {
        List<Process> readyQueue = new ArrayList<>();
        List<Process> remainingProcesses = new ArrayList<>(processes);
        float currentTime = 0;

        while (!remainingProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Add arrived processes to ready queue
            for (int i = 0; i < remainingProcesses.size(); i++) {
                Process p = remainingProcesses.get(i);
                if (p.getArrivalTime() <= currentTime) {
                    readyQueue.add(p);
                    remainingProcesses.remove(i);
                    i--;
                }
            }

            if (!readyQueue.isEmpty()) {
                // Sort by priority (lower number = higher priority)
                readyQueue.sort(Comparator.comparing(Process::getPriority));
                Process currentProcess = readyQueue.remove(0);

                // Add context switch time if this isn't the first process
                if (!completedProcesses.isEmpty()) {
                    currentTime += Config.context;
                }

                // Execute the process
                currentTime += currentProcess.getBurstTime();
                completedProcesses.add(new Process(
                        currentProcess.getName(),
                        currentProcess.getArrivalTime(),
                        currentProcess.getBurstTime(),
                        currentProcess.getPriority(),
                        currentProcess.getQuantum()
                ));
            } else {
                // No process in ready queue, advance time to next arrival
                if (!remainingProcesses.isEmpty()) {
                    currentTime = remainingProcesses.stream()
                            .map(Process::getArrivalTime)
                            .min(Integer::compareTo)
                            .orElse((int) currentTime + 1);
                }
            }
        }
    }

    @Override
    public void detailedExecutionTimeline() {
        System.out.println("\nDetailed Execution Timeline:");
        float currentTime = 0;
        Process prevProcess = null;

        for (Process p : completedProcesses) {
            // Add context switch time between processes
            if (prevProcess != null) {
                System.out.printf("Time %.1f - %.1f: Context Switch%n",
                        currentTime, currentTime + Config.context);
                currentTime += Config.context;
            }

            System.out.printf("Time %.1f: Process %s starts execution (Priority: %d)%n",
                    currentTime, p.getName(), p.getPriority());
            currentTime += p.getBurstTime();
            System.out.printf("Time %.1f: Process %s completes execution%n",
                    currentTime, p.getName());

            prevProcess = p;
        }
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        List<Process> waitingTimes = new ArrayList<>();
        float currentTime = 0;
        Process prevProcess = null;

        for (Process p : completedProcesses) {
            if (prevProcess != null) {
                currentTime += Config.context;
            }
            float waitingTime = Math.max(0, currentTime - p.getArrivalTime());
            Process waitingProcess = new Process(p.getName(), (int)waitingTime, p.getBurstTime(), p.getPriority(), p.getQuantum());
            waitingTimes.add(waitingProcess);
            System.out.printf("Process %s waiting time: %.1f%n", p.getName(), waitingTime);
            currentTime += p.getBurstTime();
            prevProcess = p;
        }

        // Calculate and display average waiting time
        float avgWaitingTime = waitingTimes.stream()
                .map(p -> (float)p.getArrivalTime()) // Using arrivalTime to store waiting time
                .reduce(0f, Float::sum) / waitingTimes.size();
        System.out.printf("Average Waiting Time: %.1f%n", avgWaitingTime);

        return waitingTimes;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        float currentTime = 0;
        Process prevProcess = null;

        for (Process p : completedProcesses) {
            if (prevProcess != null) {
                currentTime += Config.context;
            }
            currentTime += p.getBurstTime();
            float turnaroundTime = currentTime - p.getArrivalTime();
            System.out.printf("Process %s turnaround time: %.1f%n", p.getName(), turnaroundTime);
            prevProcess = p;
        }

        // Calculate and display average turnaround time
        float sumTurnaroundTime = 0;
        currentTime = 0;
        prevProcess = null;

        for (Process p : completedProcesses) {
            if (prevProcess != null) {
                currentTime += Config.context;
            }
            currentTime += p.getBurstTime();
            sumTurnaroundTime += (currentTime - p.getArrivalTime());
            prevProcess = p;
        }

        float avgTurnaroundTime = sumTurnaroundTime / completedProcesses.size();
        System.out.printf("Average Turnaround Time: %.1f%n", avgTurnaroundTime);

        return new ArrayList<>();
    }

    @Override
    public void Display_initial_process_list() {
        System.out.printf("%-10s %-12s %-12s %-12s %-8s %n",
                "Processes", "Burst-time", "Arrival-time", "Priority", "Context");
        for (Process process : processes) {
            System.out.printf("%-10s %-12d %-12d %-12d %-8.2f %n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority(),
                    Config.context
            );
        }
    }

}