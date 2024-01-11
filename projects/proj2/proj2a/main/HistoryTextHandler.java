package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import java.util.List;


public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap ngMap;
    public HistoryTextHandler(NGramMap map) {
        this.ngMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        String response = "";
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        for (String word: words) {
            String temp = word + ": ";
            response = response + temp;
            response = response + ngMap.weightHistory(word, startYear, endYear).toString();
            response += "\n";
        }
        return response;
    }
}
