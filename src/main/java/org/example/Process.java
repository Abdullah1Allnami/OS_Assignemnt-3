package org.example;

public class Process {
    private String name;
    private String color;
    private float arrivalTime;
    private float burstTime;
    private int priority;
    private float fcaiFactor;

    public Process(String name, String color, float arrivalTime, float burstTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public String getName() { return name; }
    public float getArrivalTime() { return arrivalTime; }
    public float getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public void setBurstTime(float burstTime) { this.burstTime = burstTime; }
}
