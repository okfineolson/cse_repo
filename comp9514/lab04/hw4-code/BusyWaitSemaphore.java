public class BusyWaitSemaphore implements Semaphore3151 {

    private volatile int count;
    //TODO: add private state here as needed
    public BusyWaitSemaphore(int v) {
        //TODO: Implement
        count = v;
    }
    @Override
    public void P() {
        while (count <= 0)
        {
            Thread.yield();
        }
        count--;
        //TODO: synchronized blocks can be used to do the comparison and decrement in one step.
        //TODO: Thread.yield() should be called in the busy wait

    }

    @Override
    public void V() {
        count++;
        //TODO: synchronized method or blocks can be used here to do the increment in one step.
    }
}

