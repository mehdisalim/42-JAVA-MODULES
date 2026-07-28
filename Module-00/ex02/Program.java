import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        int numberOfPrimes = 0;
        
        while (true) {
            System.out.print("-> ");
            final long input = scan.nextLong();
            if (input == 42)
                break;
            final long sumOfDigits = sumOfDigits(input);
            if (isPrime(sumOfDigits)) {
                numberOfPrimes++;
            }
        }
        System.out.println("Count of coffee-request - " + numberOfPrimes);
        scan.close();
    }

    private static long sumOfDigits(long number) {
        long sum = 0;
        for (; number != 0; number /= 10) {
            sum += number % 10;
        }
        return sum;
    }

    private static boolean isPrime(final long number) {
        if (number < 2) 
            return false;
        int i = 2;
        while (i <= number / i) {
            if (number % i == 0) {
                return false;
            }
            i++;
        }
        return true;
    }

}