import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    

    public static void main(String[] args) { 

        if (args.length < 1 || (!args[0].equals("--profile=dev") && !args[0].equals("--profile=prod"))) {
            System.err.println("Error: missing argument ");
            System.exit(-1);
        }

        final Scanner scan = new Scanner(System.in);
        final boolean isDevMode = args[0].equals("--profile=dev");
        final Menu menu = new Menu(isDevMode);

        while (true) {
            try {
                menu.displayMenu();
                System.out.print("-> ");
                if (!scan.hasNextInt()) {
                    scan.nextLine();
                    System.err.println("   Invalid Order!!");
                    System.out.println("   ---------------------------------------------------------");
                    continue ;
                }
                final int orderNumber = scan.nextInt();
                if (orderNumber > 7 || (orderNumber > 5 && !isDevMode)) {
                    System.err.println("   Invalid Order!!");
                    System.out.println("   ---------------------------------------------------------");
                    continue ;
                }
                menu.diplayOrderMessage(orderNumber);
                System.out.print("-> ");
                menu.printTheResult(orderNumber, scan);
    
            } catch (Exception e) {
                System.err.println("   Error: " + e.getMessage());
            }
            

            System.out.println("   ---------------------------------------------------------");
        }

    }
}