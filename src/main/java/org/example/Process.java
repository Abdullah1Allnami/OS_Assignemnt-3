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
    private int startTime;
    private int tornatoundTime;
    private int waiteTime;


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
    public void setPriority(int priority) { this.priority = priority; }
    public int getStartTime() { return startTime; }
    public int getTurnaroundTime() { return tornatoundTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public int getWaitTime() { return waiteTime; }
    public boolean getFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
    public void setWaitTime(int waiteTime) { this.waiteTime = waiteTime; }

}
