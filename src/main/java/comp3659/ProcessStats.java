package comp3659;

public class ProcessStats {

public int pid;
public int type;
private int burstTimeMin;
private int burstTimeMax;
private int waitTimeMax;
private int waitTimeMin;
public int timeToRun;
public int timeToWait;
public int arrivalTime;
public int roundRobinRuntime;
public int previousBurstTime;
public int predictedBurstTime;
public int numberOfCpuBursts;


public ProcessStats(int pid) {
    this.pid = pid;

    this.type = (int) (Math.random() * 3) + 1;
   // System.out.println("Process " + pid + " is of type " + type);
    this.roundRobinRuntime = 0;


if (type == 1) {
    this.burstTimeMin = 5;
    this.burstTimeMax = 10;

    this.waitTimeMin = 50;
    this.waitTimeMax = 100;
} else if (type == 2) {
    this.burstTimeMin = 15;
    this.burstTimeMax = 25;

    this.waitTimeMin = 1500;
    this.waitTimeMax = 3000;
} else {
    this.burstTimeMin = 45;
    this.burstTimeMax = 60;

    this.waitTimeMin = 3000;
    this.waitTimeMax = 6000;
}
  // System.out.println("Process " + pid + " has a max length of " + burstTimeMax + " and a min length of " + burstTimeMin);

    generateNewWaitTime();
    this.arrivalTime = pid;
    //System.out.println("Process " + pid + " arrived at " + arrivalTime);
    this.previousBurstTime = (burstTimeMax + burstTimeMin) /2;
    this.predictedBurstTime = this.previousBurstTime;
    this.numberOfCpuBursts = 0;
}










public void generateNewRunTime() {
    this.timeToRun = (int) (Math.random() * (this.burstTimeMax - this.burstTimeMin)) + this.burstTimeMin;
    return;
}
public void generateNewWaitTime() {
    this.timeToWait = (int) (Math.random() * (this.waitTimeMax - this.waitTimeMin)) + this.waitTimeMin;
    return;
}










public ProcessStats(int pid, int type) {
    this.roundRobinRuntime = 0;
    this.pid = pid;
    this.type = type;
    if (type == 1) {
        this.burstTimeMin = 5;
        this.burstTimeMax = 10;
    
        this.waitTimeMin = 50;
        this.waitTimeMax = 100;
    } else if (type == 2) {
        this.burstTimeMin = 15;
        this.burstTimeMax = 25;
    
        this.waitTimeMin = 1500;
        this.waitTimeMax = 3000;
    } else {
        this.burstTimeMin = 45;
        this.burstTimeMax = 60;
    
        this.waitTimeMin = 3000;
        this.waitTimeMax = 6000;
    }
  // System.out.println("Process " + pid + " has a max length of " + burstTimeMax + " and a min length of " + burstTimeMin);

    generateNewWaitTime();
    this.arrivalTime = pid;
    //System.out.println("Process " + pid + " arrived at " + arrivalTime);
    generateNewRunTime();
    this.previousBurstTime = (burstTimeMax + burstTimeMin) /2;
    this.predictedBurstTime = this.previousBurstTime;
    this.numberOfCpuBursts = 0;
}

}