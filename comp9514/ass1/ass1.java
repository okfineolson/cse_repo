import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ass1 {
    private final int[] numbers;

    private int size;
    private final ReentrantReadWriteLock lock;

    public static void main(String[] args) {
        ass1 myAss1 = new ass1(10);
        // test1(myAss1);
        test2(myAss1);
    }

    public ass1(int capacity) {
        this.numbers = new int[capacity];

        this.size = 0;
        this.lock = new ReentrantReadWriteLock();
    }

    public void insert(int x) {
        lock.writeLock().lock();
        try {
            if (size == numbers.length) {
                // If array is full, block until space is available
                while (size == numbers.length) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
            int index = binarySearch(x);
            if (index < size && numbers[index] == x) {
                // If number already exists, do nothing
                return;
            }
            if (index < size) {
                // Shift elements to make place for new number
                System.arraycopy(numbers, index, numbers, index + 1, size - index);

            }
            numbers[index] = x;

            size++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(int x) {
        lock.writeLock().lock();
        try {
            int index = binarySearch(x);
            if (index < size && numbers[index] == x) {
                // Mark element as deleted
                numbers[index] = -1;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean member(int x) {
        lock.readLock().lock();
        try {
            int index = binarySearch(x);
            return index < size && numbers[index] == x;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void print_sorted() {
        lock.readLock().lock();
        try {
            for (int i = 0; i < size; i++) {
                if (numbers[i] != -1) {
                    System.out.println(numbers[i]);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void cleanup() {
        lock.writeLock().lock();
        try {
            int j = 0;
            for (int i = 0; i < size; i++) {
                if (numbers[i] != -1) {
                    numbers[j] = numbers[i];
                    j++;
                }

            }
            size = j;

        } finally {
            lock.writeLock().unlock();
        }
    }

    private int binarySearch(int x) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (numbers[mid] < x) {
                low = mid + 1;
            } else if (numbers[mid] > x) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return low;
    }

    private static void test1(ass1 myAss1) {
        myAss1.insert(5);
        myAss1.insert(3);
        myAss1.insert(8);
        myAss1.insert(2);
        System.out.println("Is 3 a member? " + myAss1.member(3));
        System.out.println("Is 4 a member? " + myAss1.member(4));
        System.out.println("Sorted elements:");
        myAss1.print_sorted();
        myAss1.delete(3);
        System.out.println("Is 3 a member after deletion? " + myAss1.member(3));
        System.out.println("Sorted elements after deletion:");
        myAss1.print_sorted();
        myAss1.cleanup();
        System.out.println("Sorted elements after cleanup:");
        myAss1.print_sorted();
    }

    private static void test2(ass1 myAss1) {

        // Create and start threads that insert elements
        Thread insertThread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                myAss1.insert(i);
            }
        });
        Thread insertThread2 = new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                myAss1.insert(i);
            }
        });
        insertThread1.start();
        insertThread2.start();

        // Wait for the threads to finish
        insertThread1.join();
        insertThread2.join();

        // Print the sorted elements
        System.out.println("Sorted elements after insertion:");
        myAss1.print_sorted();

        // Create and start threads that delete elements
        Thread deleteThread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                myAss1.delete(i);
            }
        });
        Thread deleteThread2 = new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                myAss1.delete(i);
            }
        });
        deleteThread1.start();
        deleteThread2.start();

        // Wait for the threads to finish
        deleteThread1.join();
        deleteThread2.join();

        // Print the sorted elements after deletion
        System.out.println("Sorted elements after deletion:");
        myAss1.print_sorted();

        // Perform a cleanup
        myAss1.cleanup();

        // Print the sorted elements after cleanup
        System.out.println("Sorted elements after cleanup:");
        myAss1.print_sorted();
    }

}

