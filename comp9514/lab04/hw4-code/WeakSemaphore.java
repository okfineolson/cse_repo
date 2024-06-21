public class WeakSemaphore implements Semaphore3151 {
    //TODO: Add private state here as needed

    private volatile int count;
    public WeakSemaphore(int v) {
        //TODO: implement
        count = v;
    }
    @Override
    public void P() {
        if (count <= 0)
            {
                try {
                    wait();
                }
                catch(InterruptedException e) {}
            }
            count--;
            notify();
        //TODO: Used synchronized methods and the Java wait() method to add the process to the waiting set.
    }

    @Override
    public void V() {
        count++;
        notify();
        //TODO: Use the Java notify() method to awaken a waiting process.
    }

}

