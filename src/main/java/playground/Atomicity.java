package playground;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Execution: javap -c Atomicity
 *
public class playground.Atomicity {
 int i;

 public playground.Atomicity();
 Code:
 0: aload_0
 1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 4: return

 void f1();
 Code:
 0: aload_0
 1: dup
 2: getfield      #2                  // Field i:I
 5: iconst_1
 6: iadd
 7: putfield      #2                  // Field i:I
 10: return

 void f2();
 Code:
 0: aload_0
 1: dup
 2: getfield      #2                  // Field i:I
 5: iconst_3
 6: iadd
 7: putfield      #2                  // Field i:I
 10: return
}
 * in between getfield and putfield,
 * another task could modify the field
 * and operations are not atomic
 *
 * => In Java, increment is NOT atomic.
 */
public class Atomicity {
    private int i;
    void f1() { i++; }
    void f2() { i += 3; }

    private static class AtomicityIncrementer implements Runnable {
        private int i = 0;

        /**
         * Although return i is indeed an atomic operation,
         * the lack of synchronization allows the value to be read
         * while the object is in an unstable intermediate state.
         * Both getValue() and evenIncrement() should be synchronized.
         *
         * In Java, increment is NOT atomic and involves both a read
         * and a write, where there is room for threading problems.
         */
        public int getValue() { return i; }
        private synchronized void evenIncrement() { i++; i++; }
        public void run() {
            while (true) {
                evenIncrement();
            }
        }

        /** example output: 206111 */
        public static void main(String[] args) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            AtomicityIncrementer incrementer = new AtomicityIncrementer();
            executorService.execute(incrementer);
            while (true) {
                int val = incrementer.getValue();
                if (val % 2 != 0) {
                    System.out.println(val);
                    System.exit(0);
                }
            }
        }
    }

    public static class SerialNumberGenerator {
        private static volatile int serialNumber = 0;

        /**
         * **rule of synchronization**:
         *     1. make the filed volatile if it could be simultaneously accessed by multiple tasks
         *     2. must synchronize all reads and writes that access the shared object
         *     3. non-synchronized methods are free to ignore object lock
         */
        // public static int nextSerialNumber() { ... }: Not thread-safe
        public static synchronized int nextSerialNumber() {
            return serialNumber++;
        }
    }
}
