package org.example;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int quantum;
    private int startTime = 0;
    private float turnaroundTime = 0;
    private float waitTime = Integer.MAX_VALUE;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
    }

    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public void setBurstTime(int burstTime) {
        if (burstTime < 0) throw new IllegalArgumentException("Burst time cannot be negative.");
        this.burstTime = burstTime;
    }
    public int getPriority() { return priority; }
    public int getQuantum() { return quantum; }
    public void setQuantum(int quantum) {
        if (quantum < 0) throw new IllegalArgumentException("Quantum cannot be negative.");
        this.quantum = quantum;
    }
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) {
        if (startTime < 0) throw new IllegalArgumentException("Start time cannot be negative.");
        this.startTime = startTime;
    }
    public float getWaitTime() { return waitTime; }
    public void setWaitTime(float waitTime) {
        if (waitTime < 0) throw new IllegalArgumentException("Wait time cannot be negative.");
        this.waitTime = waitTime;
    }
    public float getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(float turnaroundTime) {
        if (turnaroundTime < 0) throw new IllegalArgumentException("Turnaround time cannot be negative.");
        this.turnaroundTime = turnaroundTime;
    }
}
