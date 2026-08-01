import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Downloader {
    private final ConcurrentLinkedQueue<String> listOfUrls;

    public Downloader() {
        this.listOfUrls = new ConcurrentLinkedQueue<>();
    }

    public Downloader(final ConcurrentLinkedQueue<String> listOfUrls) {
        this.listOfUrls = listOfUrls;
    }

    public synchronized void addUrl(final String url) {
        listOfUrls.add(url);
    }

    public synchronized String getNextUrl() {
        return listOfUrls.poll();
    }
    


    public void downloadTheFile(final String urlString) {
        try {
            System.out.println(Thread.currentThread().getName()
                    + " start download: " + urlString);
    
            URL url = new URL(urlString);
    
            // Extract the file name from the URL
            String fileName = Paths.get(url.getPath()).getFileName().toString();
            if (fileName.isEmpty()) {
                fileName = "downloaded_file";
            }
    
            try (InputStream in = url.openStream();
                 OutputStream out = new FileOutputStream(fileName)
            ) {
    
                byte[] buffer = new byte[8192];
                int bytesRead;
    
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                
                System.out.println(Thread.currentThread().getName()
                        + " finish download: " + fileName);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } 
    
    
        } catch (Exception e) {
            System.err.println("Failed to download " + urlString);
            System.err.println(e.getMessage());
        }
    }
}