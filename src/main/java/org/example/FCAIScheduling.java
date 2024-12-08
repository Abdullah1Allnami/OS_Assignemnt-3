
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
    List<Process> get_ready_processes(List<Process> processes, int currentTime, Process running) {
        List<Process> readyQueue = new ArrayList<>();
        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && process.getBurstTime() > 0 && process != running) {
                readyQueue.add(process);
            }
        }
        readyQueue.sort(Comparator.comparing(this::calculateFcaiFactor)); // Sort by FCAI factor
        return readyQueue;
    }


    public void print_details(Process running, int currentTime) {
        if (running == null)
            return;
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
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-15s %-25s%n",
                "Time", "Processes", "Executed-time", "Remaining-Burst",
                "Updated-Quantum", "Priority", "FCAI-Factor");

        List<Process> ready = get_ready_processes(processes, 0, null);
        Queue<Process> waiting = new LinkedList<>();
        Process running = null;
        boolean from_waiting = true;
        boolean allow_preemption = true;
        int currentTime = -1;

        while (true) {
            currentTime++;
            ready = get_ready_processes(processes, currentTime, running);
            for (Process process : ready)
                if (process.getArrivalTime() == currentTime && process != running)
                    waiting.add(process);

            if (running != null) {
                int excuted = currentTime - running.getStartTime();
                int runningQuantum = running.getQuantum();

                if (running.getBurstTime() == 0)
                    running.setTurnaroundTime(currentTime - running.getArrivalTime());

                // finish it's quantum or finished it's burst
                if (!ready.isEmpty() && excuted == runningQuantum || running.getBurstTime() == 0) {
                    if (running.getBurstTime() != 0)
                        waiting.add(running);

                    running.setQuantum(runningQuantum + 2);
                    ready.remove(running);
                    from_waiting = true;
                }
                if (excuted >= Math.ceil(runningQuantum * 0.4))
                    allow_preemption = true;

            }
            // if the last one end its quantum pick form the waiting queue
            if (from_waiting || ready.isEmpty() && !waiting.isEmpty()) {
                print_details(running, currentTime);
                running = waiting.peek();
                running.setStartTime(currentTime);
            } else if (!ready.isEmpty() && allow_preemption && calculateFcaiFactor(ready.get(0)) <= calculateFcaiFactor(running)) {
                running.setQuantum(running.getQuantum() * 2 - (currentTime - running.getStartTime()));
                waiting.add(running);
                print_details(running, currentTime);
                running = ready.get(0);
                running.setStartTime(currentTime);
            }
            waiting.remove(running);

            if (running != null) {
                if (running.getWaitTime() == Float.MAX_VALUE)
                    running.setWaitTime(currentTime - running.getArrivalTime());
                running.setBurstTime(running.getBurstTime() - 1);
            }

            if (ready.isEmpty() && waiting.isEmpty() && (running == null || running.getBurstTime() == 0)) {
                print_details(running, currentTime + 1);
                break;
            }

            allow_preemption = false;
            from_waiting = false;
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