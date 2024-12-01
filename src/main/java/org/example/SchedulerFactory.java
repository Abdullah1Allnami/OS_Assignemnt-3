package org.example;
import java.util.List;

public class SchedulerFactory {
    public static Scheduler createScheduler(String type, List<Process> processes) {
        switch (type) {
            case "Priority":
                return new PrioritySchedulingContextSwitching(processes);
            case "SJF":
                return new SJFScheduler(processes);
            case "SRTF":
                return new SRTFScheduler(processes);
            case "FCAI":
                return new FCAIScheduling(processes);
            default:
                throw new IllegalArgumentException("Invalid Scheduler Type");
        }
    }
}
