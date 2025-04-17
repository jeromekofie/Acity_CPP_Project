import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler extends KeywordSearcher {
    private String filename;

    public void setFilename(String name) {
        this.filename = name;
    }

    public String getFilename() {
        return filename;
    }

    public boolean fileExists() {
        File file = new File(filename);
        return file.exists();
    }

    public List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    public void saveToFile(List<String> lines, String outputFilename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Results saved to " + outputFilename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
