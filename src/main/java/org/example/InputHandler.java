package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private int round_robin_q;

    public List<Process> getProcessesFromFile(String fileName) {
        List<Process> processes = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));

            // Read the number of processes
            int number_of_processes = Integer.parseInt(scanner.nextLine().trim());

            // Read the round-robin time quantum
            this.round_robin_q = Integer.parseInt(scanner.nextLine().trim());

            // Read each process data
            for (int i = 0; i < number_of_processes; ++i) {
                if (scanner.hasNextLine()) {
                    String[] processDetails = scanner.nextLine().trim().split("\\s+");
                    String name = processDetails[0];
                    String color = processDetails[1];
                    float arrival_time = Float.parseFloat(processDetails[2]);
                    float brust_time = Float.parseFloat(processDetails[3]);
                    int priority = Integer.parseInt(processDetails[4]);

                    Process process = new Process(name, color, arrival_time, brust_time, priority);
                    processes.add(process);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return processes;
    }

    public int getRound_robin_q() {
        return round_robin_q;
    }
}
