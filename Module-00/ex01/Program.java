import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        System.out.print("-> ");
        final long number = scan.nextLong();
        if (!isInputValide(number)){
            scan.close();
            return ;
        }
        isPrime(number);
        scan.close();
    }


    private static void isPrime(final long number) {
        int i = 2;
        while (i < number / i) {
            if (number % i == 0) {
                System.out.println("false " + (i - 1));
                System.exit(-1);
            }
            i++;
        }
        System.out.println("true " + (i - 1));
    }

    private static boolean isInputValide(final long number) {
        if (number < 2) {
            System.err.println("IllegalArgument");
            return false;
        }
        return true;
    } 

}