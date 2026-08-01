public class Program {
    public static void main(String[] args) {
        if (args.length != 2) {
            exitProgram();
        }

        final String arraySizeArg = args[0];
        final String threadsCountArg = args[1];

        if (!arraySizeArg.startsWith("--arraySize=") || !threadsCountArg.startsWith("--threadsCount=")) {
            exitProgram();
        }

        final String[] arraySizeArgs = arraySizeArg.split("=");
        if (arraySizeArgs.length != 2) {
            exitProgram();
        }
        final String[] threadsCountArgs = threadsCountArg.split("=");
        if (threadsCountArgs.length != 2) {
            exitProgram();
        }

        final int arraySizeInt = Integer.parseInt(arraySizeArgs[1]); 
        final int threadsCountInt = Integer.parseInt(threadsCountArgs[1]); 

        final ArraySum array = new ArraySum(arraySizeInt);
        final long sum = array.calculateTheSumOfArrayValues();
        System.out.println("Sum: " + sum);

        final int chunk = (arraySizeInt + threadsCountInt - 1) / threadsCountInt;

        final Thread[] threads = new Thread[threadsCountInt];
        for (int i = 0; i < threadsCountInt; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                int start = index * chunk;
                int end;
                if (index == threadsCountInt - 1)
                    end = arraySizeInt;
                else
                    end = start + chunk;
                
                final long rangeSum = array.calculateRangeOfArrayValues(start, end);
                System.out.println("Thread " + index + ": from " + start + " to " + (end - 1) + " sum is " + rangeSum);
            }, ("Thread " + i));
        }

        for (Thread thread : threads) {
            thread.start();
        }


        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sum By threads: " + array.getSum());

    }


    private static void exitProgram() {
        System.err.println("Invalid Arguments");
        System.exit(1);
    }

}
