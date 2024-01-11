package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

import ngrams.NGramMap;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wdNet;
    private NGramMap ngram;

    public HyponymsHandler(WordNet net, NGramMap gram) {
        this.wdNet = net;
        this.ngram = gram;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        HashMap<Double, Set<String>> popularity = new HashMap<>();
        List<Double> sortedPopularity = new ArrayList<>();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        StringBuilder response = new StringBuilder("[");
        List<List<String>> seperate = new ArrayList<>();
        List<String> combined = new ArrayList<>();
        Set<String> strings = new HashSet<>();

        if (k > 0) {
            response = new StringBuilder();
            for (String word : words) {
                seperate.add(wdNet.hyponyms((word)));
            }
            combined = seperate.get(0);
            for (int i = 1; i < seperate.size(); i++) {
                combined.retainAll(seperate.get(i));
            }
            for (String hyp : combined) {
                Collection<Double> listCounts = ngram.countHistory(hyp, startYear, endYear).values();
                Double tempSum = 0.0;
                for (Double count : listCounts) {
                    tempSum += count;
                }
                if (tempSum > 0) {
                    sortedPopularity.add(tempSum);
                    if (popularity.containsKey(tempSum)) {
                        popularity.get(tempSum).add(hyp);
                    } else {
                        Set<String> newSet = new HashSet<>();
                        newSet.add(hyp);
                        popularity.put(tempSum, newSet);
                    }
                }
            }
            Collections.sort(sortedPopularity);
            Collections.reverse(sortedPopularity);
            int count = 0;
            while (count < sortedPopularity.size() && count < k) {
                strings.addAll(popularity.get(sortedPopularity.get(count)));
                count = count + popularity.get(sortedPopularity.get(count)).size();
            }
            response.append(wdNet.alphabeticalOrder(strings).toString());
            return response.toString();
        } else {
            if (words.size() > 1) {
                for (String word : words) {
                    seperate.add(wdNet.hyponyms((word)));
                }
                combined = seperate.get(0);
                for (int i = 1; i < seperate.size(); i++) {
                    combined.retainAll(seperate.get(i));
                }
                for (int i = 0; i < combined.size(); i++) {
                    response.append(combined.get(i));
                    if (i != combined.size() - 1) {
                        response.append(", ");
                    }
                }
            } else {
                List<String> hyponyms = wdNet.hyponyms(words.get(0));
                for (int i = 0; i < hyponyms.size(); i++) {
                    response.append(hyponyms.get(i));
                    if (i != hyponyms.size() - 1) {
                        response.append(", ");
                    }
                }
            }
        }
        response.append("]");
        return response.toString();
    }
}
