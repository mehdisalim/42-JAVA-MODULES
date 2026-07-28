import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program {

    final static List<Signature> signatures = new ArrayList<Signature>();

    public static void main(String[] args) {
        
        readSignatures();

        try (
            final Scanner scan = new Scanner(System.in);
            final FileOutputStream resultFile = new FileOutputStream("result.txt")
        ) {

            while (true) {
                System.out.print("-> ");
                if (!scan.hasNextLine())
                    break ;
                final String fileName = scan.nextLine();
                if (fileName.length() <= 0) {
                    continue ;
                }
                if (fileName.equals("42")) {
                    break ;
                }
                final String fileExtension = processTheFile(fileName);
                if (fileExtension == null) {
                    System.out.println("UNDEFINED");
                    continue ;
                }
                resultFile.write(fileExtension.getBytes());
                resultFile.write('\n');
                System.out.println("PROCESSED");
            }


        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
        }


    }

    private static String processTheFile(final String fileName) {
        try (
            final FileInputStream fileInputStream = new FileInputStream(fileName);
        ) {
            System.out.println("fileInputStream.getFD().valid(): " + fileInputStream.getFD().valid());
            final byte[] fileSignature = new byte[16];
            fileInputStream.read(fileSignature);
            System.out.println("fileSignature: " + fileSignature + " fileSignature length: " + fileSignature.length);
            if (fileSignature.length <= 0)
                return null;
            return getFileExtention(fileSignature);

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private static String getFileExtention(final byte[] signature) {
        for (final Signature sign : signatures) {
            
            final byte[] refrenceSignature = sign.getSignature();
            if (refrenceSignature.length > signature.length)
                continue ;
            boolean isMatch = true;
            for (int i = 0; i < refrenceSignature.length; i++) {
                if (refrenceSignature[i] != signature[i]) {
                    isMatch = false;
                    break ;
                }
            }
            if (isMatch) {
                return sign.getExtention();
            }
        }
        return null;
    }


    private static void readSignatures() {
        try (
            final FileInputStream fileInput = new FileInputStream("signatures.txt");
        ) {
            String str = "";
            while (true) {
                final int data = fileInput.read();
                if (data == -1) {
                    if (str.length() != 0) {
                        convertStringSignatureToHexaSignatureAndAddToSignatureList(str);
                        str = "";
                    }
                    break ;
                }
                if ((char)data == '\n') {
                    convertStringSignatureToHexaSignatureAndAddToSignatureList(str);
                    str = "";
                    continue ;
                }
                str = str + (char)data;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }


    private static void convertStringSignatureToHexaSignatureAndAddToSignatureList(final String str) {
        final String[] splittedString = str.split(", ");
        final String fileExtention = splittedString[0];
        final String fileSignature = splittedString[1];
        final String[] hexs = fileSignature.split(" ");
        final byte[] bSignature = new byte[hexs.length];
        for (int i = 0; i < hexs.length; i++) {
            bSignature[i] = (byte)Integer.parseInt(hexs[i], 16);
        }
        signatures.add(new Signature(fileExtention, bSignature));
    }

}