import java.io.*;
import java.nio.file.*;

public class DirectoryReader {

    public static void main(String[] args) {
        String directoryPath = "/path/to/directory";
        String outputFilePath = "combined_output.txt";
        String encryptionKey = "YourSecretEncryptionKey";

        try {
            // Step 1: Write all files to a single file
            writeFilesToSingleFile(directoryPath, outputFilePath);

            // Step 2: Read the file content and encrypt it
            String fileContent = new String(Files.readAllBytes(Paths.get(outputFilePath)));
            String encryptedContent = AESUtils.encrypt(fileContent, encryptionKey);

            // Step 3: Save the encrypted content back to the file
            Files.write(Paths.get(outputFilePath), encryptedContent.getBytes());

            System.out.println("Files have been encrypted and written to " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFilesToSingleFile(String directoryPath, String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(filePath -> {
                    try {
                        writer.write("---START OF FILE---\n");
                        writer.write("FilePath: " + filePath.toString() + "\n");
                        writer.write("---CONTENT---\n");
                        Files.lines(filePath).forEach(line -> {
                            try {
                                writer.write(line + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        writer.write("---END OF FILE---\n\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        writer.close();
    }
}

public class KeyGeneratorExample {
    public static void main(String[] args) {
        try {
            String key = AESUtils.generateKey();
            System.out.println("Generated AES Key: " + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
