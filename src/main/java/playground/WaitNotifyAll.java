package playground;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * One worker to turn on, the other to turn off.
 * The 2nd worker cannot do its job until the 1st one is on.
 * The 1st worker must wait until the off worker is finished before it can turn on.
 * 1 -> 2 -> 1 -> ...
 *
 * **must surround wait() with a while loop**
 * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#wait-long-
 *
 * synchronized (obj) {
 *     while (<condition does not hold>)
 *         obj.wait(timeout);
 *     ... // Perform action appropriate to condition
 * }
 */
class SharedWorker {
    private volatile boolean worker = false;
    public synchronized void workerOn(){
        worker = true;
        // all tasks waiting on the particular lock are awoken;
        notifyAll();
    }
    public synchronized void workerOff(){
        worker = false;
        notifyAll();
    }
    public synchronized void waitForWorkerOn() throws InterruptedException {
        while(!worker)
            wait();
    }
    public synchronized void waitForWorkerOff() throws InterruptedException {
        // TODO: why it must go inside while(condition does not hold) loop
        /*
         * a thread might wake up when the condition does not hold;
         * notifyAll() may awake another WorkerOn which is waiting;
         * so the lock is already grabbed from current task
         */
        while(worker)
            wait();
    }

    public synchronized boolean getWorker() { return worker; }
}

class WorkerOn implements Runnable {
    private SharedWorker sharedWorker;
    public WorkerOn(SharedWorker w) { sharedWorker = w; }
    public void run() {
        try {
            // same effect of leaving loop with an exception
            while(!Thread.interrupted()) {
                System.out.println(String.format(
                        "Worker on %s - %s", Thread.currentThread().toString(), sharedWorker.getWorker()));
                TimeUnit.MILLISECONDS.sleep(180);  // no release object lock
                sharedWorker.workerOn();
                sharedWorker.waitForWorkerOff();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending ");
    }
}

class WorkerOff implements Runnable {
    private SharedWorker sharedWorker;
    public WorkerOff(SharedWorker w) { sharedWorker = w; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                sharedWorker.waitForWorkerOn();
                System.out.println("Worker off");
                TimeUnit.MILLISECONDS.sleep(200);  // no release object lock
                sharedWorker.workerOff();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
    }
}

public class WaitNotifyAll {
    /**
     * Worker on
     * Worker off
     * ...
     * Worker on
     * Worker off
     * Exiting via interrupt
     * Exiting via interrupt
     * Ending
     */
    public static void main(String[] args) throws InterruptedException {
        SharedWorker worker = new SharedWorker();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WorkerOff(worker));
        executorService.execute(new WorkerOn(worker));
        executorService.execute(new WorkerOn(worker));
        TimeUnit.SECONDS.sleep(3);
        executorService.shutdownNow();  // Interrupt all tasks
    }
}