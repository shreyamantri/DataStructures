package main;
import java.util.*;

public class Graph {
    //variables : what is our graph representation
    // adjList, adjMatrix

    //First HashMap: maps ID to the Node --> synsets
    //Second HashMap: maps the String to the Node IDs --> hyponyms

    public static class Node {
        Integer ID;
        String text;
        ArrayList<Integer> children;
        public Node() {
            this.ID = 0;
            this.text = "";
            children = new ArrayList<>();
        }
    }
    HashMap<Integer, ArrayList<Node>> synsetValues;

    public Graph() {
        synsetValues = new HashMap<>();
    }

    public void insertSynset(Integer key, Integer value) { //Maps the String in the synsets file to an ID
        if (synsetValues.containsKey(key)) {
            Node temp = new Node();
            temp.ID = value;
            synsetValues.get(key).add(temp);
        } else {
            Node newVal = new Node();
            newVal.ID = value;
            ArrayList<Node> newArr = new ArrayList<>();
            newArr.add(newVal);

            synsetValues.put(key, newArr);
        }
    }

    /**public ArrayList<Node> getHyper(String key) {
        if (!synsetValues.containsKey(key)) {
            return null;
        }
        return synsetValues.get(key);
    }*/

    public String youDoSomething() {
        return "Hello";
    }

}
