# NGordnet (Wordnet)

## Goal of Project:

- Create an implementation of the NGordnet tool, using the WordNet dataset to understand what words are hyponyms of each other 

## Files to Look At + My Task:

# Graph.java
- Creates two hashmap objects, one maps the word ID to the word, and the other maps the word to the ID
- The HyponymsHandler class uses the Graph class to access the ID-word or word-ID relationship

# HyponymsHandler.java 

- Basic case: returns a string representation of a list of the hyponyms of the single world
- Handling list of words: if you input multiple words, the NGordnet tool will return all the hyponyms that are common between the two words
- k != 0: code works like the above two functions, except it only outputs the first k hyponyms
