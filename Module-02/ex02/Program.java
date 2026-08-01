import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Program {

    private static File currentPath;

    public static void main(String[] args) {

        if (args.length != 1 || !args[0].startsWith("--current-folder=")) {
            System.err.println("Usage: java Program --current-folder=<absolute-path>");
            return;
        }

        currentPath = new File(args[0].substring("--current-folder=".length()));

        if (!currentPath.exists() || !currentPath.isDirectory()) {
            System.err.println("Invalid directory.");
            return;
        }

        Scanner scan = new Scanner(System.in);
        
        System.out.println(currentPath.getAbsolutePath());

        while (true) {

            System.out.print("-> ");

            String line = scan.nextLine().trim();

            if (line.isEmpty())
                continue;

            String[] cmd = line.split("\\s+");

            switch (cmd[0]) {

                case "ls":
                    displayFolderContent();
                    break;

                case "cd":
                    if (cmd.length == 2)
                        changeDir(cmd[1]);
                    else
                        System.out.println("Usage: cd <folder>");
                    break;

                case "mv":
                    if (cmd.length == 3)
                        moveFile(cmd[1], cmd[2]);
                    else
                        System.out.println("Usage: mv <source> <destination>");
                    break;

                case "exit":
                    scan.close();
                    return;

                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    private static void changeDir(String folder) {

        File newDir = new File(currentPath, folder);

        if (!newDir.exists() || !newDir.isDirectory()) {
            System.out.println("Directory not found.");
            return;
        }

        try {
            currentPath = newDir.getCanonicalFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void moveFile(String source, String destination) {

        Path sourcePath = new File(currentPath, source).toPath();

        File destFile = new File(currentPath, destination);

        if (destFile.exists() && destFile.isDirectory()) {
            destFile = new File(destFile, sourcePath.getFileName().toString());
        }

        try {
            Files.move(
                    sourcePath,
                    destFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayFolderContent() {

        File[] files = currentPath.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            long size = getSize(file);
            System.out.printf("%-20s %d KB%n", file.getName(), size / 1024);
        }
    }

    private static long getSize(File file) {

        if (file.isFile())
            return file.length();

        long total = 0;

        File[] children = file.listFiles();

        if (children != null) {
            for (File child : children)
                total += getSize(child);
        }

        return total;
    }
}