# LinkedListDeque

## Goal of Project:

- Create a representation of a LinkedListDeque, with a sentinel pointing to the first element and each node pointing to the next node until there's a null value

## Files to Look At + My Task:

# LinkedListDeque.java

- addFirst(): add nodes to the front of the LinkedList Deque, readjusting pointers such that the added element is the first element in the Deque and now points to original first item
- addLast(): add nodes to the end of the LinkedList Deque, adjusting pointers such that the original last item now points to the added element
- toList(): create a string representation of the LinkedList for testing purposes
- isEmpty(): returns True if Deque is empty
- size(): returns the size of the Deque, or the number of Nodes
- get(int index): return the value of the Node at the given index
- getRecursive(int index): return the value of the Node at the given index using a recursive process
- removeFirst(): remove the first Node in the LinkedListDeque, readjusting pointers
- removeLast(): remove the last Node in the LinkedListDeque, readjusting pointers
