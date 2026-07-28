import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        System.out.print("-> ");
        final long number = scan.nextLong();
        if (number < 2){
            System.err.println("IllegalArgument");
            scan.close();
            System.exit(-1);
        }
        isPrime(number);
        scan.close();
    }


    private static void isPrime(final long number) {
        int i = 2;
        while (i <= number / i) {
            if (number % i == 0) {
                System.out.println("false " + (i - 1));
                return ;
            }
            i++;
        }
        System.out.println("true " + (i - 1));
    }
    
}