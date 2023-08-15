package comp3659;

//import comp3659.*;
// import comp3659.SimProcesses.BasicAdd;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.util.concurrent.TimeUnit;

public class SchedSim {
    public static ArrayList<ProcessStats> processRunQueue;
    public static ArrayList<ProcessStats> processWaitQueue;
    public static int runningProcessIndex = 0;
    public static int numberOfProcesses = 0;
    public static int totalNumberOfClockTicks;
    public static int currentNumberOfClockTicks;
    public static int timeQuantum = 10;
    public static int timeQuantumCounter = 0;
    public static int FCFSflag = 1;
    public static int preemptiveSJFflag = 2;
    public static int nonpreemptiveSJFflag = 3;
    public static int RRflag = 4;
    private static int throughput = 0;
    private static int averageWaitingTime = 0;
    private static int longestWaitingTime = 0;
    private static double alphaValue = 0.5;

    public static void chart_main(String[] args) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Thing 1", new Double(20));
        dataset.setValue("Thing 2", new Double(50));

        JFreeChart chart = ChartFactory.createPieChart(
                "Title",
                dataset,
                true,
                true,
                false);

        int width = 640;
        int height = 480;
        File chartimg = new File("chart.jpeg");
        try {
            ChartUtils.saveChartAsJPEG(chartimg, chart, width, height);
        } catch (IOException e) {
        }
    }

    static class windowSetup implements ActionListener {

        JFrame f = new JFrame("Scheduler");
        public JTextField box1 = new JTextField();
        public JTextField box2 = new JTextField();
        public JTextField box3 = new JTextField();
        JLabel prompt1, prompt2, prompt3;
        JButton b1;

        windowSetup() {
        
    
            prompt1 = new JLabel("Enter time quantum:");
            prompt1.setBounds(20, 100, 200, 30);
            box1 = new JTextField("");
            box1.setBounds(200, 100, 150, 30);
    
            prompt2 = new JLabel("Enter number of processes:");
            prompt2.setBounds(20, 150, 200, 30);
            box2 = new JTextField("");
            box2.setBounds(200, 150, 150, 30);
    
            prompt3 = new JLabel("Enter total time to run:");
            prompt3.setBounds(20, 200, 200, 30);
            box3 = new JTextField("");
            box3.setBounds(200, 200, 150, 30);
    
            b1 = new JButton("Go");
            b1.setBounds(200, 250, 150, 30);

            b1.addActionListener(this);
            f.add(box1);
            f.add(box2);
            f.add(box3);
            f.add(prompt1);
            f.add(prompt2);
            f.add(prompt3);
            f.add(b1);
            f.setSize(400, 400);
            f.setLayout(null);
            f.setVisible(true);

        }
    
        public void actionPerformed(ActionEvent e) {
            String s1 = box1.getText();
            String s2 = box2.getText();
            String s3 = box3.getText();

            timeQuantum = Integer.parseInt(s1);
            numberOfProcesses = Integer.parseInt(s2);
            totalNumberOfClockTicks=Integer.parseInt(s3);
        
            System.out.println(timeQuantum);
            System.out.println(numberOfProcesses);
            System.out.println(totalNumberOfClockTicks);

            // int timequantum = Integer.parseInt(s1);
            // int processes = Integer.parseInt(s2);
            // int totalNumberOfClockTicks = Integer.parseInt(s3);
            // System.out.println("Time quantum is: " + timequantum);
            // System.out.println("Number of processes is: " + processes);
            // System.out.println("Number of clock ticks is: " + totalNumberOfClockTicks);

        }}
    
    public static void main(String[] args) {

       new windowSetup();

       try {
        TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
       
       // numberOfProcesses = 10;
        initializeProcessQueues();

        ArrayList<ProcessStats> copy = new ArrayList<ProcessStats>(numberOfProcesses);
        copyQueue(copy, processRunQueue);

        // System.out.println("Enter the number of \"clock ticks\" to simulate");
        totalNumberOfClockTicks = 1000;

        //System.out.println("FCFS");
        throughput = 0;
        averageWaitingTime = 0;
        longestWaitingTime = 0;
        runFirstComeFirstServe();
        copyQueue(processRunQueue, copy);
        for (int i = 0; i < numberOfProcesses; i++) {
            processWaitQueue.set(i, null);
        }
        int throughputFCFS = throughput;
        int averageWaitingTimeFCFS = averageWaitingTime;
        int longestWaitingTimeFCFS = longestWaitingTime;

       // System.out.println("SJF");
        throughput = 0;
        averageWaitingTime = 0;
        longestWaitingTime = 0;
        runningProcessIndex = findShortestJob();
        runShortestJobFirst();
        copyQueue(processRunQueue, copy);
        for (int i = 0; i < numberOfProcesses; i++) {
            processWaitQueue.set(i, null);
        }
        int throughputSJF = throughput;
        int averageWaitingTimeSJF = averageWaitingTime;
        int longestWaitingTimeSJF = longestWaitingTime;

      //  System.out.println("RR");
        throughput = 0;
        runRoundRobin();
        int throughputRR = throughput;
        int averageWaitingTimeRR = averageWaitingTime;
        int longestWaitingTimeRR = longestWaitingTime;

        System.out.println("FCFS: " + throughputFCFS);
      System.out.println("SJF: " + throughputSJF);
       System.out.println("RR: " + throughputRR);
        // SchedSim s = new SchedSim();
        // s.runSim();
    }
    
    private static void initializeProcessQueues() {
        processRunQueue = new ArrayList<ProcessStats>(numberOfProcesses);
        processWaitQueue = new ArrayList<ProcessStats>(numberOfProcesses);
        for (int i = 0; i < numberOfProcesses; i++) {
            processWaitQueue.add(i, null);
            processRunQueue.add(i, new ProcessStats(i));
            processRunQueue.get(i).generateNewRunTime();
           // System.out.println("Process " + processRunQueue.get(i).pid + " has reentered the run queue with length "
               //     + processRunQueue.get(i).timeToRun);
        }
        return;
    }

    private static void copyQueue(ArrayList<ProcessStats> copy, ArrayList<ProcessStats> source) {
        for (int i = 0; i < numberOfProcesses; i++) {
            copy.add(i, new ProcessStats(source.get(i).pid, source.get(i).type));
        }
        return;
    }

    private static void runFirstComeFirstServe() {

        currentNumberOfClockTicks = 0;

    //    System.out.println("Process " + processRunQueue.get(runningProcessIndex).pid + " is now running");
        while (currentNumberOfClockTicks < totalNumberOfClockTicks) {
            // System.out.print(".");
            updateRunningProcess(FCFSflag);
            updateWaitQueue();
            currentNumberOfClockTicks++;
        }
    }

    private static void runShortestJobFirst() {
        currentNumberOfClockTicks = 0;

    //    System.out.println("Process " + processRunQueue.get(runningProcessIndex).pid + " is now running");
        while (currentNumberOfClockTicks < totalNumberOfClockTicks) {
            // System.out.print(".");
            updateRunningProcess(preemptiveSJFflag);
            updateWaitQueue();
            currentNumberOfClockTicks++;
        }
    }

    private static void runRoundRobin() {
        currentNumberOfClockTicks = 0;

     //   System.out.println("Process " + processRunQueue.get(runningProcessIndex).pid + " is now running");
        while (currentNumberOfClockTicks < totalNumberOfClockTicks) {
            // System.out.print(".");
            updateRunningProcess(RRflag);
            updateWaitQueue();
            currentNumberOfClockTicks++;
        }
    }

    private static void updateRunningProcess(int flag) {
        ProcessStats runningProcess = processRunQueue.get(runningProcessIndex);
        runningProcess.timeToRun--;
        if (runningProcess.timeToRun == 0) {
            runningProcessFinished(runningProcess, flag);
            timeQuantumCounter = 0;
        } else {
            if (flag == 2) {
                runningProcessIndex = findShortestJob();
            } else if (flag == 4) {
                if (timeQuantumCounter == timeQuantum) {
                  //  System.out.println("tq");
                    runningProcessIndex = findRoundRobin();
             //       System.out.println("Process " + processRunQueue.get(runningProcessIndex).pid + " is now running");
                    timeQuantumCounter = 0;
                } else {
                    timeQuantumCounter++;
                }
            }
        }
        // if(runningProcess.timeToRun == 0){
        // runningProcessFinished(runningProcess, flag);
        // }
    }

    public static void runningProcessFinished(ProcessStats runningProcess, int algorithmSelect) {
      //  System.out.println("Process " + runningProcess.pid + " has finished running");
        throughput++;
        runningProcess.numberOfCpuBursts++;
        runningProcess.generateNewWaitTime();
        processWaitQueue.set(runningProcessIndex, runningProcess);
        processRunQueue.set(runningProcessIndex, null);
        if (algorithmSelect == 1) {
            runningProcessIndex = findFirstArrival();
        } else if (algorithmSelect == 2 || algorithmSelect == 3) {
            runningProcessIndex = findShortestJob();
        } else if (algorithmSelect == 4) {
            runningProcessIndex = findRoundRobin();
        }
        runningProcess = processRunQueue.get(runningProcessIndex);
        runningProcess.previousBurstTime = runningProcess.timeToRun;
    //    System.out.println("Process " + processRunQueue.get(runningProcessIndex).pid + " is now running");
    }

    public static void updateWaitQueue() {
        for (int i = 0; i < processWaitQueue.size(); i++) {
            if (processWaitQueue.get(i) != null) {
                ProcessStats process = processWaitQueue.get(i);
                process.timeToWait--;
                if (process.timeToWait == 0) {
                    process.arrivalTime = currentNumberOfClockTicks;
                    process.generateNewRunTime();
          //          System.out.println("Process " + process.pid + " has reentered at " + process.arrivalTime
            //                + " the run queue with length " + process.timeToRun);
                    processRunQueue.set(i, process);
                    processWaitQueue.set(i, null);
                }
            }
        }
    }

    private static int findFirstArrival() {
        int firstNonNullIndex = firstNonNullEntry(0);
        int lowestArrival = processRunQueue.get(firstNonNullIndex).arrivalTime;
        int lowestArrivalIndex = firstNonNullIndex;
        for (int i = firstNonNullIndex; i < numberOfProcesses; i++) {
            if (processRunQueue.get(i) != null && processRunQueue.get(i).arrivalTime < lowestArrival) {
                lowestArrival = processRunQueue.get(i).arrivalTime;
                lowestArrivalIndex = i;
            }

        }

        return lowestArrivalIndex;
    }

    private static int findShortestJob() {
        int firstNonNullIndex = firstNonNullEntry(0);
        int shortestJobIndex = firstNonNullIndex;
        int shortestJob = processRunQueue.get(firstNonNullIndex).predictedBurstTime;

        for (int i = firstNonNullIndex; i < numberOfProcesses; i++) {
            if (processRunQueue.get(i) != null && processRunQueue.get(i).predictedBurstTime < shortestJob) {
                shortestJob = processRunQueue.get(i).predictedBurstTime;
                shortestJobIndex = i;
            }
        }
        return shortestJobIndex;
    }

    private static int findRoundRobin() {
        int index = runningProcessIndex + 1;
        int roundRobinIndex = firstNonNullEntry(index);

        return roundRobinIndex;
    }

    // create a function that returns the index of the first non null entry in the
    // processRunQueue
    private static int firstNonNullEntry(int start) {
        int index = findFirstInNonEmptyQueue(start);
        while (index == -1) {
            currentNumberOfClockTicks++;

            updateWaitQueue();
            index = findFirstInNonEmptyQueue(start);
        }
        return index;
    }

    private static int findFirstInNonEmptyQueue(int start) {
        int index = -1;
        for (int i = start; i < numberOfProcesses; i++) {
            if (processRunQueue.get(i) != null) {
                return i;
            }
        }
        for (int i = 0; i < start; i++) {
            if (processRunQueue.get(i) != null) {
                return i;
            }
        }

        return index;
    }
}
