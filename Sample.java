import java.io.*;
import java.nio.file.*;

public class DirectoryReader {

    public static void main(String[] args) {
        String directoryPath = "/path/to/directory";
        String outputFilePath = "combined_output.txt";
        try {
            writeFilesToSingleFile(directoryPath, outputFilePath);
        } catch (IOException e) {
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
