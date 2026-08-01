import java.util.Random;


public class ArraySum {

    private final long[] array;
    private final long arraySize;
    private volatile long sum = 0;

    public ArraySum(final int arraySize) {
        this.arraySize = arraySize;
        this.array = new long[arraySize];
        final Random ran = new Random();
        for (int i = 0; i < arraySize; i++) {
            final long ranDouble = ran.nextInt(1000);
            // array[i] = ranDouble;
            array[i] = 1;
        }
    }


    public long calculateTheSumOfArrayValues() {
        long sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }


    private synchronized void increamentSum(final long additionValue) {
        this.sum += additionValue;
    }

    public long calculateRangeOfArrayValues(final int start, final int end) {
        if (start < 0 || start > arraySize || start > end) {
            throw new IllegalArgumentException("Invalid Params");
        }
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];    
        }
        increamentSum(sum);       
        return sum;
    }

    public synchronized long getSum() {
        return this.sum;
    }

}
