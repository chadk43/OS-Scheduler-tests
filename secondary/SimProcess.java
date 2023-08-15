// package comp3659;

// public abstract class SimProcess extends Thread{
//         @SuppressWarnings("deprecation") //resume and suspend are deprecated, but no idea what to do without

//         private boolean suspended = false;
//         private boolean terminated = false;
//         public boolean isTerminated() { return this.terminated; }

//         public void startOrResume() {
//             if (!this.isTerminated()){
//                 if (suspended) {
//                  this.resume();
//                 suspended = false;
//             } else {
//                 this.start();
//             }
//             }
//         }

//         /**
//          * Suspend thread*/
//         public void pause() {
//             this.suspend();
//             suspended = true;
//         }

//         public void run() {
//             procRun();
//             this.terminated = true;
//         }

//         abstract public void procRun();

//     }

