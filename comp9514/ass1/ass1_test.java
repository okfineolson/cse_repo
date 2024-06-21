public class ass1_test {
    public static void main(String[] args) {
        testConcurrentInsertAndMember();
        testConcurrentDelete();
    }

    public void testConcurrentInsertAndMember() throws InterruptedException {
        final int NUM_THREADS = 10;
        final int NUM_NUMBERS = 100;
        ass1 set = new ass1(NUM_NUMBERS);
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * (NUM_NUMBERS / NUM_THREADS);
            final int end = (i + 1) * (NUM_NUMBERS / NUM_THREADS);
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    set.insert(j);
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }
        for (int i = 0; i < NUM_NUMBERS; i++) {
            assert (set.member(i));
        }
    }

    public void testConcurrentDelete() throws InterruptedException {
        final int NUM_THREADS = 10;
        final int NUM_NUMBERS = 100;
        ass1 set = new ass1(NUM_NUMBERS);
        for (int i = 0; i < NUM_NUMBERS; i++) {
            set.insert(i);
        }
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * (NUM_NUMBERS / NUM_THREADS);
            final int end = (i + 1) * (NUM_NUMBERS / NUM_THREADS);
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    set.delete(j);
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }
        for (int i = 0; i < NUM_NUMBERS; i++) {
            assert (!set.member(i));
        }
    }
}

