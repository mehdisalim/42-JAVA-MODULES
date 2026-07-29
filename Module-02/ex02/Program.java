import java.io.File;
import java.util.Scanner;

public class Program {


    private static File file;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No argument provided!");
            System.exit(-1);
        }

        
        if (!args[0].startsWith("--current-folder=")) {
            System.err.println("Invalid argument!");
            System.exit(-1);
        }
        final String absolutePath = args[0].substring(17);
        file = new File(absolutePath);
        System.out.println("   " + file.getAbsolutePath());

        final Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("   " + file.getAbsolutePath());
            System.out.print("-> ");
            final String command = scan.nextLine().trim();
            final String[] commandArgs = command.split(" ");
            if (commandArgs[0].equals("mv")) {
                moveFile(commandArgs[1], commandArgs[2]);
            } else if (commandArgs[0].equals("ls")) {
                displayFolderContent();
            } else if (commandArgs[0].equals("cd")) {
                
            } else if (commandArgs[0].equals("exit")) {
                break ;
            } else {
                System.err.println("   Invalid Command, try again with one of those: ls, mv, cd or exit");
            }
        }

        scan.close();

    }


    private static void moveFile(final String source, final String destination) {
        final Path sourcePath = Paths.

    }

    private static void displayFolderContent() {

        File[] fileStores = file.listFiles();
        try {
            for (File fileStore : fileStores) {
                System.out.println("   " + fileStore.getName() + " " + (fileStore.getTotalSpace() / 1024) + " KB");
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


}