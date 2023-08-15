// //package comp3659;

// import java.util.ArrayList;
// import java.util.Iterator;
// import java.time.LocalTime;
// import comp3659.SimProcess;
// import java.time.temporal.ChronoUnit;

// public class ShortestJobFirst {

//    // @SuppressWarnings("deprecation") //resume and suspend are deprecated, but no idea what to do without
   
//     private ArrayList<SimProcess> readyQueue;
//     private Iterator<SimProcess> procIter;
//     private long runtime;
//     private long elapsed;
//     private long SJF_milis;

//     public static final long DEF_SJF_MILIS = 4;
   
//     /**
//      * Classic round-robin scheduler
//      * @param procsToRun List of processes to run
//      * @param runtime How long (seconds) to run the simulation
//      * @param rr_milis Per-process time quantum (Default = {@value DEF_RR_MILIS})
//      */
//     public void ShortestJobFirst(ArrayList<SimProcess> procsToRun, long runtime, long SJF_milis) {
//         if (procsToRun.isEmpty()) {
//             throw new IllegalArgumentException("Empty process list");
//         }
        
//         this.runtime = runtime;
//         this.readyQueue = procsToRun;
//         this.procIter = readyQueue.iterator();
//         this.SJF_milis = sjf_milis;
//     }

//     // /**
//     //  * 
//     //  * @param procsToRun
//     //  * @param runtime
//     //  */
//     // public void RoundRobin(ArrayList<SimProcess> procsToRun, long runtime) {
//     //     this(procsToRun, runtime, DEF_RR_MILIS);
//     // }

//     public void run() throws InterruptedException{
//         LocalTime start = LocalTime.now();
//         long elapsed_secs = 0;
//         Collection.sort(readyQueue);
//         SimProcess p = readyQueue.get(0);
//         p.startOrResume();
//         while (p != null && elapsed_secs < runtime) {
//             //waits at most rr_millis for thread to complete, otherwise switch to next
//             p.join(this.rr_milis);
//             p = expireAndNext(p);
//             elapsed_secs = start.until(LocalTime.now(), ChronoUnit.SECONDS);
//         }
//         cleanup();
//     }

//     private SimProcess expireAndNext(SimProcess p) {
//         if (readyQueue.size() > 1) {
//             //no need to switch when only one process
//             p.pause();
//             p = next();
//             if (p != null) {
//                 p.startOrResume();
//             }
//         }
//         return p;
//     }

//     private SimProcess next() {
//         if (!procIter.hasNext()) {
//             //go back to beginning
//             procIter = readyQueue.iterator();
//         }
//         SimProcess p;
//         while (procIter.hasNext()) {
//             p = procIter.next();
//             if (!p.isTerminated()) {
//                 //return next available proc
//                 return p;
//             }
//         }
//         return null;
//     }


//     private void cleanup() {
//         //in case sim ends before all tasks run
//         for (Thread t : readyQueue) {
//             t.stop();
//         }
//     }
// }
