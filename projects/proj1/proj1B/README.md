# ArrayDeque

## Goal of Project:

- Implement a Deque interface that uses a backing array instead of linked nodes

## Files to Look At + What I Did:

# ArrayDeque.java

- addFirst(): add elements to the front of the Deque, takes constant time
- addLast(): add elements to the end of the Deque, takes constant time
- resize(): resize the array if there are no more spaces to add a new value, takes constant time
- get(int index): get the value at the current index in the array
- getRecurisve(int index): get the value at the current index using recursion
- isEmpty(): returns True if the backing array is empty
- size(): returns the size of the backing array
- removeFirst(): removes the first element in the array
- removeLast(): removes the last element in the array
- resizeDown(): resize the array down, making sure that the memory the program uses is proportional to the number of items in the array
