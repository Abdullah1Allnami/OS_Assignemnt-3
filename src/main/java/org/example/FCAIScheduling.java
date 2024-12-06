
package org.example;

import java.util.*;

public class FCAIScheduling implements Scheduler {
    private static float V1;
    private static float V2;
    private List<Process> processes;

    // Constructor to initialize the processes and compute V1 and V2
    FCAIScheduling(List<Process> processes) {
        this.processes = processes;
        V1 = computeV1(processes);
        V2 = computeV2(processes);
    }

    // Method to compute V1 based on the last arrival time of all processes
    private float computeV1(List<Process> processes) {
        float lastArrivalTime = Float.NEGATIVE_INFINITY;
        for (Process process : processes) {
            if (process.getArrivalTime() > lastArrivalTime) {
                lastArrivalTime = process.getArrivalTime();
            }
        }
        return lastArrivalTime / 10;
    }

    // Method to compute V2 based on the maximum burst time of all processes
    private float computeV2(List<Process> processes) {
        float maxBurstTime = Float.NEGATIVE_INFINITY;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurstTime) {
                maxBurstTime = process.getBurstTime();
            }
        }
        return maxBurstTime / 10;
    }

    // Calculate FCAI factor for each process
    private float calculateFcaiFactor(Process process) {
        return (float) Math.ceil((10 - process.getPriority()) +
                (process.getArrivalTime() / V1) +
                (process.getBurstTime() / V2));
    }

    // Get the ready queue of processes that are ready to run based on current time
    List<Process> get_ready_processes(List<Process> processes, int currentTime) {
        List<Process> readyQueue = new ArrayList<>();
        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && process.getBurstTime() > 0) {
                readyQueue.add(process);
            }
        }
        readyQueue.sort(Comparator.comparing(this::calculateFcaiFactor)); // Sort by FCAI factor
        return readyQueue;
    }


    public void print_details(Process running, int currentTime) {
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-15s %-25s%n",
                (running.getStartTime() + " - " + (currentTime)),
                running.getName(),
                currentTime - running.getStartTime(),
                running.getBurstTime(),
                running.getQuantum(),
                running.getPriority(),
                calculateFcaiFactor(running));
    }

    @Override
    public void detailedExecutionTimeline() {
        List<Process> processes1 = new ArrayList<Process>(processes);
        int currentTime = 0;
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-15s %-25s%n",
                "Time", "Processes", "Executed-time", "Remaining-Burst",
                "Updated-Quantum", "Priority", "FCAI-Factor");

        Process waiting = null;
        while (!processes1.isEmpty()) { // Ensure loop ends
            List<Process> readyQueue = get_ready_processes(processes1, currentTime);
            if (readyQueue.isEmpty()) break;
            Process running = readyQueue.get(0);
            if (waiting != null) {
                processes1.add(waiting);
                waiting = null;
            }
            running.setStartTime(currentTime);

            if (running.getWaitTime() == Integer.MAX_VALUE)
                running.setWaitTime(currentTime - running.getArrivalTime());


            int runningQuantum = running.getQuantum();
            int burstBefore = running.getBurstTime();

            // Execute process and allow preemption check
            for (int i = 1; running.getBurstTime() > 0 && i <= runningQuantum; i++) {
                running.setBurstTime(running.getBurstTime() - 1);
                currentTime++;
                if (i >= Math.ceil(runningQuantum * 0.4)) { // Check preemption after 40%
                    List<Process> updatedQueue = get_ready_processes(processes1, currentTime);
                    if (i == runningQuantum && running.getBurstTime() != 0) {
                        waiting = running;
                        processes1.remove(running);
                        running.setQuantum(runningQuantum + 2);

                        // handel if there is one process waiting and no process ready
                        if(processes1.size() == 0 && waiting != null) {
                            processes1.add(waiting);
                            waiting = null;
                        }
                    }

                    if (!updatedQueue.isEmpty() && calculateFcaiFactor(updatedQueue.get(0)) < calculateFcaiFactor(running)) {
                        running.setQuantum(runningQuantum * 2 - i);
                        break;
                    }
                }
            }
            if (running.getBurstTime() == 0) {
                running.setTurnaroundTime(currentTime - running.getArrivalTime());
                processes1.remove(running);
            }
            print_details(running, currentTime);
        }
    }


    @Override
    public List<Process> calculateWaitingTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes available to calculate waiting time.");
            return processes;
        }

        float totalWaitingTime = 0;
        System.out.printf("%-10s %-12s%n", "Processes", "WaitingTime");
        for (Process process : processes) {
            System.out.printf("%-10s %-12.2f%n", process.getName(), process.getWaitTime());
            totalWaitingTime += process.getWaitTime();
        }
        System.out.printf("Average Waiting Time: %.2f%n", totalWaitingTime / processes.size());
        return processes;
    }

    @Override
    public List<Process> calculateTurnaroundTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes available to calculate turnaround time.");
            return processes;
        }

        float totalTurnaroundTime = 0;
        System.out.printf("%-10s %-12s%n", "Processes", "TurnaroundTime");
        for (Process process : processes) {
            System.out.printf("%-10s %-12.2f%n", process.getName(), process.getTurnaroundTime());
            totalTurnaroundTime += process.getTurnaroundTime();
        }
        System.out.printf("Average Turnaround Time: %.2f%n", totalTurnaroundTime / processes.size());
        return processes;
    }


    @Override
    public void Display_initial_process_list() {
        System.out.printf("%-10s %-12s %-12s %-12s %-8s %-20s%n",
                "Processes", "Burst-time", "Arrival-time", "Priority",
                "Quantum", "Initial-FCAI-Factors");
        for (Process process : processes) {
            System.out.printf("%-10s %-12d %-12d %-12d %-8d %-20.2f%n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getPriority(),
                    process.getQuantum(),
                    calculateFcaiFactor(process)
            );
        }
    }
}