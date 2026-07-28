import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Program {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid argument");
            return ;
        }

        try (
            final BufferedReader input1 = new BufferedReader(new FileReader(args[0]));
            final BufferedReader input2 = new BufferedReader(new FileReader(args[1]));
        ) {
            
        } catch (IOException e) {
            // TODO: handle exception
        }

    }
}
