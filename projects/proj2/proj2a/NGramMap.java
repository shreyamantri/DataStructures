package ngrams;

import java.util.Collection;
import edu.princeton.cs.algs4.In;
import java.util.Map;
import java.util.TreeMap;


import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private Map<String, TimeSeries> wordMap;
    private TimeSeries countMap;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);
        wordMap = new TreeMap<>();
        countMap = new TimeSeries();

        while (words.hasNextLine()) {
            String nextWordLine = words.readLine();
            String[] splitWordLine = nextWordLine.split("\t");

            String word = splitWordLine[0];
            Integer year = Integer.parseInt(splitWordLine[1]);
            Double countWord = Double.parseDouble(splitWordLine[2]);

            if (wordMap.containsKey(word)) {
                wordMap.get(word).put(year, countWord);
            } else {
                TimeSeries tempObj = new TimeSeries();
                tempObj.put(year, countWord);
                wordMap.put(word, tempObj);
            }
        }

        while (counts.hasNextLine()) {
            String nextCountLine = counts.readLine();
            String[] splitCountLine = nextCountLine.split(",");
            Integer countYear = Integer.parseInt(splitCountLine[0]);
            Double totalWords = Double.parseDouble(splitCountLine[1]);
            countMap.put(countYear, totalWords);
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries tempObj = new TimeSeries();

        for (String words: wordMap.keySet()) {
            if (words.equals(word)) {
                TimeSeries valueAtWord = wordMap.get(word);

                for (Integer year: valueAtWord.keySet()) {
                    if (year >= startYear && year <= endYear) {
                        tempObj.put(year, valueAtWord.get(year));
                    }
                }
            }
        }

        return tempObj;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        TimeSeries newObj = new TimeSeries();

        for (String words: wordMap.keySet()) {
            if (words.equals(word)) {
                newObj = wordMap.get(word);
            }
        }

        return newObj;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries newObject = new TimeSeries();

        for (Integer year: countMap.keySet()) {
            if (year >= MIN_YEAR && year <= MAX_YEAR) {
                newObject.put(year, countMap.get(year));
            }
        }

        return newObject;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries valueAtWord = wordMap.get(word);

        if (valueAtWord == null) {
            return new TimeSeries();
        }

        return new TimeSeries(valueAtWord.dividedBy(countMap), startYear, endYear);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {

        TimeSeries tempObj = new TimeSeries();

        for (String word: words) {
            tempObj = tempObj.plus(weightHistory(word, startYear, endYear));

        }
        return tempObj;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {

        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }
}
