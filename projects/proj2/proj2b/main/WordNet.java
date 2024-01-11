package main;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class WordNet {
    private Graph graph;
    private Map<Integer, ArrayList<String>> synsets; //IDs to multiple words
    private Map<String, ArrayList<Integer>> backSynsets; //words to multiple IDs

    private Set<String> hypoValues;
    public WordNet(String hyponyms, String synsetsString) {
        In hyponym = new In(hyponyms);
        In synset = new In(synsetsString);
        In newSynset = new In(synsetsString);
        graph = new Graph();
        synsets = new HashMap<>();
        backSynsets = new HashMap<>();
        hypoValues = new HashSet<>();

        while (synset.hasNextLine()) {
            String[] splitLine = synset.readLine().split(",");
            Integer ID = Integer.parseInt(splitLine[0]);
            String word = splitLine[1];
            String[] splitWord = word.split(" ");

            if (splitWord.length >= 1) {
                for (int i = 0; i<splitWord.length; i++) {
                    if (synsets.containsKey(ID)) {
                        synsets.get(ID).add(splitWord[i]);
                    } else {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(splitWord[i]);
                        synsets.put(ID, temp);
                    }
                }
            }
        }

        while (newSynset.hasNextLine()) {
            String[] splitLine = newSynset.readLine().split(",");
            Integer ID = Integer.parseInt(splitLine[0]);
            String word = splitLine[1];
            String[] splitWord = word.split(" ");

            for (int i = 0; i<splitWord.length; i++) {
                if (backSynsets.containsKey(splitWord[i])) {
                    backSynsets.get(splitWord[i]).add(ID);
                } else {
                    ArrayList<Integer> tempID = new ArrayList<>();
                    tempID.add(ID);
                    backSynsets.put(splitWord[i], tempID);
                }
            }
        }

        while (hyponym.hasNextLine()) {
            String[] splitIDs = hyponym.readLine().split(",");

            for (int i = 0; i<splitIDs.length; i++) {
                if (i != 0) {
                    graph.insertSynset(Integer.valueOf(splitIDs[0]), Integer.parseInt(splitIDs[i]));
                }
            }
        }
    }

    public List<String> hyponyms(String hypernym) { //go through the backSynsets and get the IDs for each word
        hypoValues = new HashSet<>();
        Set<String> finalValues = new HashSet<>();

        if (backSynsets.containsKey(hypernym)) {
            ArrayList<Integer> wordID = backSynsets.get(hypernym);
            for (int i = 0; i < wordID.size(); i++) {
                if (graph.synsetValues.containsKey(wordID.get(i))) {
                    for (int j = 0; j < graph.synsetValues.get(wordID.get(i)).size(); j++) {
                        Integer idValue = graph.synsetValues.get(wordID.get(i)).get(j).ID;
                        ArrayList<String> wordsFromID = synsets.get(idValue);
                        hypoValues.addAll(wordsFromID);

                        depthFirstSearch(idValue);
                    }
                }
                hypoValues.addAll(synsets.get(wordID.get(i)));
            }
            //hypoValues.addAll(synsets.get(wordID.get(0)));
        }
        //finalValues = hypoValues;
        //finalValues.add(hypernym);

        return alphabeticalOrder(hypoValues);
    }

    public void depthFirstSearch(Integer idOfNode) {

        if (graph.synsetValues.get(idOfNode) == null) {
            return;
        } else {
            if (graph.synsetValues.containsKey(idOfNode)) {
                for (int j = 0; j < graph.synsetValues.get(idOfNode).size(); j++) {
                    Integer idValue = graph.synsetValues.get(idOfNode).get(j).ID;
                    ArrayList<String> wordsFromID = synsets.get(idValue);
                    hypoValues.addAll(wordsFromID);
                    depthFirstSearch(idValue);
                }
            }
        }
    }

    public List<String> alphabeticalOrder(Set<String> hypWords) {

        List<String> sortedList = new ArrayList<>(hypWords);
        Collections.sort(sortedList);
        return sortedList;
    }

    public String doSomething() {
        return graph.youDoSomething();
    }
}
