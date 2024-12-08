package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFScheduler implements Scheduler {
    private List<Process> processes;
    private List<Process> completedProcesses;
    private static final float AGING_FACTOR = 0.1f; // Factor to increase priority based on waiting time

    SJFScheduler(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.completedProcesses = new ArrayList<>();
        executeProcesses();
    }

    private float calculateDynamicPriority(Process process, float currentTime) {
        float waitingTime = currentTime - process.getArrivalTime();
        // The longer a process waits, the lower its effective burst time becomes
        return process.getBurstTime() - (waitingTime * AGING_FACTOR);
    }

    private void executeProcesses() {
        List<Process> remainingProcesses = new ArrayList<>(processes);
        List<Process> readyQueue = new ArrayList<>();
        float currentTime = 0;
        Process currentProcess = null;
        int remainingBurstTime = 0;

        while (!remainingProcesses.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            // Add arrived processes to ready queue
            updateReadyQueue(remainingProcesses, readyQueue, currentTime);

            // If we have a current process, add it back to ready queue to compare with new arrivals
            if (currentProcess != null) {
                currentProcess.setBurstTime(remainingBurstTime);
                readyQueue.add(currentProcess);
                currentProcess = null;
            }

            if (!readyQueue.isEmpty()) {
                // Sort ready queue by dynamic priority (considering both burst time and waiting time)
                final float sortTime = currentTime;
                readyQueue.sort(Comparator.comparingDouble(p -> calculateDynamicPriority(p, sortTime)));
                
                // Get the process with highest dynamic priority
                Process nextProcess = readyQueue.remove(0);
                
                if (currentProcess == null || nextProcess != currentProcess) {
                    // Process switch occurred
                    if (currentProcess != null) {
                        System.out.printf("Time %.1f: Process %s preempted by Process %s (Remaining Time: %d)%n",
                                currentTime, currentProcess.getName(), nextProcess.getName(), remainingBurstTime);
                    }
                    currentProcess = nextProcess;
                    remainingBurstTime = currentProcess.getBurstTime();
                    
                    // Calculate waiting time
                    float waitingTime = currentTime - currentProcess.getArrivalTime();
                    if (waitingTime > 0) {
                        currentProcess.setWaitTime((int) waitingTime);
                    }
                    
                    System.out.printf("Time %.1f: Process %s starts/resumes execution (Remaining Time: %d)%n",
                            currentTime, currentProcess.getName(), remainingBurstTime);
                }

                // Execute for 1 time unit
                currentTime++;
                remainingBurstTime--;

                if (remainingBurstTime == 0) {
                    // Process completed
                    System.out.printf("Time %.1f: Process %s completes execution%n",
                            currentTime, currentProcess.getName());
                    
                    // Calculate turnaround time
                    float turnaroundTime = currentTime - currentProcess.getArrivalTime();
                    currentProcess.setTurnaroundTime(turnaroundTime);
                    
                    // Add to completed processes
                    completedProcesses.add(currentProcess);
                    currentProcess = null;
                }
            } else {
                // No process in ready queue, advance time to next arrival
                if (!remainingProcesses.isEmpty()) {
                    float nextArrival = remainingProcesses.stream()
                            .map(Process::getArrivalTime)
                            .min(Integer::compareTo)
                            .orElse((int) currentTime + 1);
                    System.out.printf("Time %.1f: CPU Idle until %.1f%n", currentTime, nextArrival);
                    currentTime = nextArrival;
                }
            }
        }
    }

    private void updateReadyQueue(List<Process> remainingProcesses, List<Process> readyQueue, float currentTime) {
        for (int i = 0; i < remainingProcesses.size(); i++) {
            Process p = remainingProcesses.get(i);
            if (p.getArrivalTime() <= currentTime) {
                readyQueue.add(p);
                remainingProcesses.remove(i);
                i--;
            }
        }
    }

    @Override
    public void Display_initial_process_list() {
        System.out.printf("%-10s %-12s %-12s %-12s%n", 
                "Process", "Burst Time", "Arrival Time", "Priority");
        for (Process p : processes) {
            System.out.printf("%-10s %-12d %-12d %-12d%n",
                    p.getName(),
                    p.getBurstTime(),
                    p.getArrivalTime(),
                    p.getPriority());
        }
    }

    @Override
    public void detailedExecutionTimeline() {
        System.out.println("\nDetailed Execution Timeline:");
        float currentTime = 0;
        
        for (Process p : completedProcesses) {
            if (currentTime < p.getArrivalTime()) {
                float idleTime = p.getArrivalTime();
                System.out.printf("Time %.1f - %.1f: CPU Idle%n",
                        currentTime, idleTime);
                currentTime = idleTime;
            }
            
            float startTime = currentTime;
            float endTime = startTime + p.getBurstTime();
            
            System.out.printf("Time %.1f: Process %s starts execution%n",
                    startTime, p.getName());
            System.out.printf("Time %.1f: Process %s completes execution%n",
                    endTime, p.getName());
            
            currentTime = endTime;
        }
    }

    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        float totalWaitingTime = 0;
        System.out.println("\nWaiting Times:");
        
        for (Process p : completedProcesses) {
            System.out.printf("Process %s waiting time: %f%n",
                    p.getName(), p.getWaitTime());
            totalWaitingTime += p.getWaitTime();
        }
        
        float avgWaitingTime = totalWaitingTime / completedProcesses.size();
        System.out.printf("Average Waiting Time: %.2f%n", avgWaitingTime);
        
        return completedProcesses;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        float totalTurnaroundTime = 0;
        System.out.println("\nTurnaround Times:");
        
        for (Process p : completedProcesses) {
            float processTurnaroundTime = p.getBurstTime() + p.getWaitTime();
            System.out.printf("Process %s turnaround time: %.1f%n",
                    p.getName(), processTurnaroundTime);
            totalTurnaroundTime += processTurnaroundTime;
        }
        
        float avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();
        System.out.printf("Average Turnaround Time: %.2f%n", avgTurnaroundTime);
        
        return completedProcesses;
    }
}
