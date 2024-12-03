package org.example;

public class Process {
    private String name;
    private float arrivalTime;
    private float burstTime;
    private int priority;
    private int quantum;


    public Process(String name, float arrivalTime, float burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
    }

    public String getName() { return name; }
    public float getArrivalTime() { return arrivalTime; }
    public float getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public void setBurstTime(float burstTime) { this.burstTime = burstTime; }
    public int getQuantum() { return quantum; }

}
