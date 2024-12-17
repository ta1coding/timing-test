import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ParallelVsSingleThreaded {

    public static void main(String[] args) {
        int dataSize = 100_000_000; // Size of the data list

        // Generate a large list of integers
        List<Integer> data = generateLargeList(dataSize);

        // Measure time for single-threaded execution
        long startTimeSingle = System.nanoTime();
        long sumSingleThread = sumSingleThreaded(data);
        long endTimeSingle = System.nanoTime();

        System.out.println("Single-threaded sum: " + sumSingleThread);
        System.out.println("Single-threaded time (ms): " + 
                           TimeUnit.NANOSECONDS.toMillis(endTimeSingle - startTimeSingle));

        // Measure time for parallel execution
        long startTimeParallel = System.nanoTime();
        long sumParallel = sumParallel(data);
        long endTimeParallel = System.nanoTime();

        System.out.println("Parallel sum: " + sumParallel);
        System.out.println("Parallel time (ms): " + 
                           TimeUnit.NANOSECONDS.toMillis(endTimeParallel - startTimeParallel));
    }

    // Generate a large list of integers
    private static List<Integer> generateLargeList(int size) {
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i + 1); // Add numbers 1, 2, ..., size
        }
        return list;
    }

    // Single-threaded sum
    private static long sumSingleThreaded(List<Integer> data) {
        long sum = 0;
        for (int num : data) {
            sum += num;
        }
        return sum;
    }

    // Parallel sum using parallelStream
    private static long sumParallel(List<Integer> data) {
        return data.parallelStream().mapToLong(Integer::longValue).sum();
    }
}
