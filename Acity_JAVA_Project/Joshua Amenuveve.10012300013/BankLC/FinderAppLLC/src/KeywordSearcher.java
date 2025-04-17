/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joshua_veve
 */
import java.util.ArrayList;

public class KeywordSearcher {
    private ArrayList<String> results = new ArrayList<>();
    private ArrayList<Integer> lineNumbers = new ArrayList<>();

    public void search(ArrayList<String> lines, String keyword) {
        results.clear();
        lineNumbers.clear();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(keyword)) {
                results.add(lines.get(i));
                lineNumbers.add(i + 1);
            }
        }
    }

    public ArrayList<String> getResults() {
        ArrayList<String> display = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            display.add("Line " + lineNumbers.get(i) + ": " + results.get(i));
        }
        return display;
    }
}