import java.util.ArrayList;

public class KeywordSearcher {
    public ArrayList<String> search(ArrayList<String> lines, String keyword) {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                results.add("Line " + (i + 1) + ": " + line.trim());
            }
        }
        return results;
    }
}
