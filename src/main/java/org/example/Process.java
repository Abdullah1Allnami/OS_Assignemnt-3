package org.example;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int quantum;
    private boolean finished;
    private int waitingTime;
    private float turnaroundTime;


    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.finished = false;
    }

    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getQuantum() { return quantum; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(float turnaroundTime) {this.turnaroundTime = turnaroundTime;}
    public int getWaitingTime() { return waitingTime; }
    public void setQuantum(int quantum) { this.quantum = quantum; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }
}
