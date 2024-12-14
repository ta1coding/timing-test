import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

public class ParallelVsRegular {

    private static final int ARRAY_SIZE = 1_000_000;

    public static void main(String[] args) {
        int[] numbers = new int[ARRAY_SIZE];
        Random random = new Random();

        // Populate the array with random numbers
        for (int i = 0; i < ARRAY_SIZE; i++) {
            numbers[i] = random.nextInt(100);
        }

        // Measure time for the regular version
        long startTime = System.currentTimeMillis();
        long regularSum = regularSum(numbers);
        long regularTime = System.currentTimeMillis() - startTime;

        System.out.println("Regular Sum: " + regularSum);
        System.out.println("Regular Time: " + regularTime + " ms");

        // Measure time for the parallel version
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        startTime = System.currentTimeMillis();
        long parallelSum = forkJoinPool.invoke(new ParallelSumTask(numbers, 0, ARRAY_SIZE));
        long parallelTime = System.currentTimeMillis() - startTime;

        System.out.println("Parallel Sum: " + parallelSum);
        System.out.println("Parallel Time: " + parallelTime + " ms");
    }

    // Regular version: Sequential sum
    public static long regularSum(int[] array) {
        long sum = 0;
        for (int value : array) {
            sum += value;
        }
        return sum;
    }

    // Parallel version: ForkJoin RecursiveTask
    static class ParallelSumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000; // Threshold for splitting tasks
        private final int[] array;
        private final int start;
        private final int end;

        public ParallelSumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // Compute directly
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // Split the task
                int mid = start + (end - start) / 2;
                ParallelSumTask leftTask = new ParallelSumTask(array, start, mid);
                ParallelSumTask rightTask = new ParallelSumTask(array, mid, end);

                leftTask.fork(); // Start the left task asynchronously
                long rightResult = rightTask.compute(); // Compute the right task synchronously
                long leftResult = leftTask.join(); // Wait for the left task

                return leftResult + rightResult;
            }
        }
    }
}
