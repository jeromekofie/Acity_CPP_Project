import java.util.ArrayList;
import java.util.List;

public class KeywordSearcher {
    protected List<Result> results = new ArrayList<>();

    public static class Result {
        int lineNumber;
        String lineText;

        public Result(int lineNumber, String lineText) {
            this.lineNumber = lineNumber;
            this.lineText = lineText;
        }

        @Override
        public String toString() {
            return "Line " + lineNumber + ": " + lineText;
        }
    }

    public void search(List<String> lines, String keyword) {
        results.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(keyword)) {
                results.add(new Result(i + 1, lines.get(i)));
            }
        }
    }

    public List<String> getResults() {
        List<String> extractedResults = new ArrayList<>();
        for (Result res : results) {
            extractedResults.add(res.toString());
        }
        return extractedResults;
    }
}
