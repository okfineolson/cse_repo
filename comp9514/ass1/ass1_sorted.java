import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class SortedArraySet {
    private int[] array;
    private int size;
    private ReentrantLock lock;

    public SortedArraySet(int capacity) {
        array = new int[capacity];
        size = 0;
        lock = new ReentrantLock();
    }

    public void insert(int x) throws InterruptedException {
        lock.lock();
        try {
            while (size == array.length) {
                // Wait until there is space available
                Thread.sleep(10);
            }
            int pos = binarySearch(x);
            if (pos < 0) {
                pos = -(pos + 1);
            }
            System.arraycopy(array, pos, array, pos + 1, size - pos);
            array[pos] = x;
            size++;
        } finally {
            lock.unlock();
        }
    }

    public void delete(int x) {
        lock.lock();
        try {
            int pos = binarySearch(x);
            if (pos >= 0) {
                array[pos] = Integer.MIN_VALUE;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean member(int x) {
        lock.lock();
        try {
            int pos = binarySearch(x);
            return pos >= 0;
        } finally {
            lock.unlock();
        }
    }

    public void printSorted() {
        lock.lock();
        try {
            for (int i = 0; i < size; i++) {
                if (array[i] != Integer.MIN_VALUE) {
                    System.out.println(array[i]);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void cleanup() {
        lock.lock();
        try {
            int j = 0;
            for (int i = 0; i < size; i++) {
                if (array[i] != Integer.MIN_VALUE) {
                    array[j++] = array[i];
                }
            }
            size = j;
        } finally {
            lock.unlock();
        }
    }

    private int binarySearch(int x) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = array[mid];

            if (midVal == Integer.MIN_VALUE || midVal < x)
                low = mid + 1;
            else if (midVal > x)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1); // key not found
    }
}
public static void main(String[] args) {
    
}