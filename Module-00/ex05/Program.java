import java.util.Scanner;

public class Program {
    
    final static private int CLASS_HOUR_INDEX = 0;
    final static private int CLASS_DAY_INDEX = 1;

    final static private int MAX_NUMBER_OF_STUDENTS = 10;
    final static private int DAYS_OF_MONTH = 30;
    final static private int TOTAL_CLASSES_PER_WEEK = 10;

    final static private String[] DAYS_OF_WEEK = {"TU", "WE", "TH", "FR", "SA", "SU", "MO"};

    private static String[] studentsNames = new String[MAX_NUMBER_OF_STUDENTS];
    private static int[][] classesTime = new int[TOTAL_CLASSES_PER_WEEK][2];

    
    private static int[][][] attendance = new int[MAX_NUMBER_OF_STUDENTS][DAYS_OF_MONTH + 1][7];
    private static Scanner scan;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        
        readStudents();
        readTimeAndDayOfWeek();
        readAttendance();
        printTimeTable();

        scan.close();
    }

    static private void printTimeTable() {

        System.out.printf("%10s", "");
        for (int dayOfMonth = 1; dayOfMonth <= DAYS_OF_MONTH; dayOfMonth++) {
            final int dayOfWeekIndex = (dayOfMonth - 1) % 7;
            for (int[] session : classesTime) {
                if (session[CLASS_DAY_INDEX] == dayOfWeekIndex) {
                    System.out.printf("%d:00 %s %2d|", session[CLASS_HOUR_INDEX], DAYS_OF_WEEK[dayOfWeekIndex], dayOfMonth);
                }
            }
        }
        System.out.println();

        for (int i = 0; i < studentsNames.length; i++) {
            System.out.printf("%10s", studentsNames[i]);
            for (int dayOfMonth = 1; dayOfMonth <= DAYS_OF_MONTH; dayOfMonth++) {
                final int dayOfWeekIndex = (dayOfMonth - 1) % 7;
                for (int[] session : classesTime) {
                    if (session[CLASS_DAY_INDEX] == dayOfWeekIndex) {
                        int status = attendance[i][dayOfMonth][session[CLASS_HOUR_INDEX]];
                        if (status == 1) {
                            System.out.printf("%10d|", 1);
                        } else if (status == -1) {
                            System.out.printf("%10d|", -1);
                        } else {
                            System.out.printf("%10s|", "");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    static private void readStudents() {
        int numberOfStudents = 0;
        System.out.print("-> ");
        while (scan.hasNextLine()) {
            final String line = scan.nextLine().trim();
            if (line.equals(".")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            if (numberOfStudents >= MAX_NUMBER_OF_STUDENTS) {
                closeProgram(scan);
            }
            studentsNames[numberOfStudents++] = line;
            System.out.print("-> ");
        }
        
        final String[] newListOfStudents = new String[numberOfStudents];
        for (int i = 0; i < numberOfStudents; i++) {
            newListOfStudents[i] = studentsNames[i];
        }
        studentsNames = newListOfStudents;
    }

    static private void readTimeAndDayOfWeek() {
        int numberOfClasses = 0;

        System.out.print("-> ");
        while (scan.hasNext()) {
            if (scan.hasNextInt()) {
                if (numberOfClasses >= TOTAL_CLASSES_PER_WEEK) {
                    closeProgram(scan);
                }
                classesTime[numberOfClasses][CLASS_HOUR_INDEX] = scan.nextInt();
                
                final String day = scan.next();
                int indexOfDayInDayOfWeek = -1;
                for (int i = 0; i < DAYS_OF_WEEK.length; i++) {
                    if (DAYS_OF_WEEK[i].equals(day)) {
                        indexOfDayInDayOfWeek = i;
                        break;
                    }
                }
                if (indexOfDayInDayOfWeek == -1) {
                    closeProgram(scan);
                }
                classesTime[numberOfClasses][CLASS_DAY_INDEX] = indexOfDayInDayOfWeek;
                numberOfClasses++;
            } else {
                String token = scan.next();
                if (token.equals(".")) {
                    break;
                } else {
                    closeProgram(scan);
                }
            }
            System.out.print("-> ");
        }
        
        final int[][] tmpList = new int[numberOfClasses][2];
        for (int i = 0; i < numberOfClasses; i++) {
            tmpList[i][CLASS_HOUR_INDEX] = classesTime[i][CLASS_HOUR_INDEX];
            tmpList[i][CLASS_DAY_INDEX] = classesTime[i][CLASS_DAY_INDEX];
        }
        
        for (int i = 0; i < numberOfClasses - 1; i++) {
            for (int j = 0; j < numberOfClasses - i - 1; j++) {
                if (tmpList[j][CLASS_DAY_INDEX] > tmpList[j + 1][CLASS_DAY_INDEX] ||
                    (tmpList[j][CLASS_DAY_INDEX] == tmpList[j + 1][CLASS_DAY_INDEX] && 
                     tmpList[j][CLASS_HOUR_INDEX] > tmpList[j + 1][CLASS_HOUR_INDEX])) {
                    int[] temp = tmpList[j];
                    tmpList[j] = tmpList[j + 1];
                    tmpList[j + 1] = temp;
                }
            }
        }
        classesTime = tmpList;
    }

    static private void readAttendance() {
        System.out.print("-> ");
        while (scan.hasNext()) {
            final String studentName = scan.next();
            if (studentName.equals(".")) {
                return;
            }
            
            int studentNameIndex = -1;
            for (int j = 0; j < studentsNames.length; j++) {
                if (studentsNames[j].equals(studentName)) {
                    studentNameIndex = j;
                    break;
                }
            }
            
            if (!scan.hasNextInt()) closeProgram(scan);
            int hour = scan.nextInt();
            
            if (!scan.hasNextInt()) closeProgram(scan);
            int day = scan.nextInt();
            
            if (!scan.hasNext()) closeProgram(scan);
            final String isPresent = scan.next();
            int isHere = 0;
            if (isPresent.equals("HERE")) {
                isHere = 1;
            } else if (isPresent.equals("NOT_HERE")) {
                isHere = -1;
            } else {
                closeProgram(scan);
            }
            
            if (studentNameIndex == -1 || hour < 1 || hour > 6 || day < 1 || day > DAYS_OF_MONTH) {
                closeProgram(scan);
            }

            attendance[studentNameIndex][day][hour] = isHere;
            System.out.print("-> ");

        }
    }

    final static private void closeProgram(final Scanner scan) {
        System.err.println("IllegalArgument");
        if (scan != null) {
            scan.close();
        }
        System.exit(1);
    }
}
