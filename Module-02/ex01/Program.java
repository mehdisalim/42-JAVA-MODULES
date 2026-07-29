import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


public class Program {
    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Invalid argument");
            return ;
        }

        try (
            final BufferedReader input1 = new BufferedReader(new FileReader(args[0]));
            final BufferedReader input2 = new BufferedReader(new FileReader(args[1]));
            final FileWriter output = new FileWriter("dictionary.txt");
        ) {
            final String firstText = input1.readLine();
            final String secondText = input2.readLine();

            final Set<String> setOfWords = new TreeSet<>();
            final String[] firstListOfWords = firstText.split(" ");
            for (String string : firstListOfWords) {
                setOfWords.add(string);
            }
            final String[] secondListOfWords = secondText.split(" ");
            for (String string : secondListOfWords) {
                setOfWords.add(string);
            }

            final List<Integer> a = new ArrayList<Integer>(setOfWords.size()); 
            final List<Integer> b = new ArrayList<Integer>(setOfWords.size()); 
            
            setOfWords.stream().forEach(t -> {
                final int aVal = frequencyOfOccurrence(firstListOfWords, t);
                a.add(aVal);
                final int bVal = frequencyOfOccurrence(secondListOfWords, t);
                b.add(bVal);
                try {
                    output.append(t + ", ");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });

            final double similarity = getSimilarityValue(a, b);
            System.out.println("Similarity = " + similarity);

        } catch (IOException e) {
            
        }

    }
    

    private static double getSimilarityValue(final List<Integer> a, List<Integer> b) {
        long numerator = 0;
        double aSquare = 0;
        double bSquare = 0;
        for (int i = 0; i < a.size(); i++) {
            int aValue = a.get(i);
            int bValue = b.get(i);
            aSquare += (aValue * aValue);
            bSquare += (bValue * bValue);
            numerator += (aValue * bValue);
        }
        double denominator = Math.sqrt(aSquare) * Math.sqrt(bSquare);
        return Math.floor((numerator / denominator) * 100.0) / 100.0;

    }

    private static int frequencyOfOccurrence(final String[] list, final String val) {
        int i = 0;
        for (String string : list) {
            if (string.equals(val))
                i++;
        }
        return i;
    }
}
