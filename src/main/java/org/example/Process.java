package org.example;

public class Process {
    private String name;
    private int arrivalTime;
    private float burstTime;
    private int priority;
    private int quantum;
    private boolean finished;


    public Process(String name, int arrivalTime, float burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.finished = false;
    }

    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public float getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public void setBurstTime(float burstTime) { this.burstTime = burstTime; }
    public int getQuantum() { return quantum; }

}
