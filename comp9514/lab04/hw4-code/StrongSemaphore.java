public class StrongSemaphore implements Semaphore3151 {
    private volatile int count;
    //TODO: Private state (presumably a Queue goes here)
    public StrongSemaphore(int v) {
        //TODO: Implement
        count = v;
    }
    @Override
    public synchronized void P() {
        //TODO: Use Java's weak waiting here here, but also add an identifier to the queue
        //TODO: All processes on waking should check if they are first in the queue.
        if (count <= 0)
            {
                try {
                    wait();
                }
                catch(InterruptedException e) {}
            }
            count--;
            (if count == 1){
                notify();
            }
        }
    }

    @Override
    public synchronized void V() {
        //TODO: Use Java's notifyAll() method to awaken all processes, but be sure to
        //TODO: make sure that all but the first process in the queue go back to sleep.
        count++;
        
        notifyAll();
    }
}

