import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        final Scanner scan = new Scanner(System.in);
        long chartValues = 0;
        for (int counter = 1; counter <= 18; counter++) {
            System.out.print("-> ");
            if (!scan.hasNextLine()) 
                break ;

            final String week = scan.nextLine();
            if (week.equals("42"))
                break ;
        
            if (!("Week " + counter).equals(week)) {
                closeProgram(scan);
            }

            chartValues *= 10;
            chartValues += getSmallestValueOfTheWeek(scan);;
            scan.nextLine();
            
        }
        scan.close();
        printTheResult(chartValues);
    }


    private static long getSmallestValueOfTheWeek(final Scanner scan) {
        System.out.print("-> ");
        int smallestValue = 9;
        for (int j = 0; j < 5 && scan.hasNextInt(); j++) {
            final int testValue = scan.nextInt();
            if (testValue > 9 || testValue < 1) {
                closeProgram(scan);
            }
            if (testValue < smallestValue)
                smallestValue = testValue;
        }
        return smallestValue;
    }

    private static int printTheResult(long values) {
        if (values == 0)
            return 0;
        final long weekValue = values % 10;
        int weekNumber = printTheResult(values / 10) + 1;
        System.out.print("Week " + weekNumber + " ");
        for (int i = 0; i < weekValue; i++) {
            System.out.print("=");
        }
        System.out.println(">");
        return weekNumber;
    }

    private static void closeProgram(final Scanner scan) {
        System.err.println("IllegalArgument");
        scan.close();
        System.exit(-1);
    }

}
