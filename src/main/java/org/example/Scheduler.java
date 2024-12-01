package org.example;

import java.util.List;

public interface Scheduler {
    // Implements the core scheduling logic for the specific algorithm
    void schedule();
    // Outputs or logs a detailed timeline of process execution, showing when each process starts and ends
    void detailedExecutionTimeline();
    // Computes the waiting time for each process based on the algorithm's scheduling order.
    List<Process> calculateWaitingTime(List<Process> processes);
    // Computes the turnaround time for each process, which is the total time a process spends in the system
    List<Process> calculateTurnaroundTime(List<Process> processes);
    // Display the initial processes list
    void Display_initial_process_list();
}
