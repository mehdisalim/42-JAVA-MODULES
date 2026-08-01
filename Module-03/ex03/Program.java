import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Program {


    public static void main(String[] args) {
        if (args.length < 1 || !args[0].startsWith("--threadsCount=")) {
            System.err.println("Invalid Arguments");
            System.exit(1);
        }
        final String[] strs = args[0].split("=");
        if (strs.length != 2) {
            System.err.println("Invalid Arguments");
            System.exit(1);
        }

        final int numberOfThreads = Integer.parseInt(strs[1]);
        final Downloader downloader = new Downloader();

        try (
            final BufferedReader reader = new BufferedReader(new FileReader("files_urls.txt"));
        ) {
            while (true) {
                final String line = reader.readLine();
                if (line == null)
                    break ;
                System.out.println("line: " + line);
                downloader.addUrl(line);
            }
            final Thread[] threads = new Thread[numberOfThreads]; 
            for (int i = 0; i < numberOfThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    while(true) {
                        final String url = downloader.getNextUrl();
                        if (url == null)
                            break ;
                        downloader.downloadTheFile(url);
                        System.out.println();
                        System.out.println();
                        System.out.println();
                    }
                });
            }

            for (Thread th : threads) {
                th.start();
            }

        } catch(final Exception e) {
            System.err.println(e.getMessage());
        }



    }
}