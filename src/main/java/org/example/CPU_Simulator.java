package org.example;

import java.util.List;

import java.util.Scanner;

public class CPU_Simulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a Scheduler:");
        System.out.println("1. Non-preemptive Priority Scheduling");
        System.out.println("2. Non-Preemptive Shortest Job First (SJF)");
        System.out.println("3. Shortest Remaining Time First (SRTF)");
        System.out.println("4. FCAI Scheduling");
        int choice = scanner.nextInt();

        String schedulerType = switch (choice) {
            case 1 -> "Priority";
            case 2 -> "SJF";
            case 3 -> "SRTF";
            case 4 -> "FCAI";
            default -> throw new IllegalArgumentException("Invalid Choice");
        };

        InputHandler inputHandler = new InputHandler();
        List<Process> processes = inputHandler.getProcessesFromFile("/Users/bdallhsydbdallh/Library/Mobile Documents/com~apple~CloudDocs/4-OS/Labs/OS_Assignment-3/src/main/java/org/example/input.txt", choice);

        Scheduler scheduler = SchedulerFactory.createScheduler(schedulerType, processes);

        System.out.println("\nInitial Processes list:");
        scheduler.Display_initial_process_list();

        System.out.println("\nScheduling Processes Start:");
        scheduler.schedule();

        System.out.println("\nExecution Timeline:");
        scheduler.detailedExecutionTimeline();

        System.out.println("\nFinal Results:");

        System.out.println("Waiting Times:");
        scheduler.calculateWaitingTime(processes);

        System.out.println("\nTurnaround Times:");
        scheduler.calculateTurnaroundTime(processes);
    }
}
