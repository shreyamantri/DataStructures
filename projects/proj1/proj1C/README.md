# Deque Enhancements

## Goal of Project:

- Create a new implementation of a Deque: MaxArrayDeque 

## Files to Look At + What I Did:

# LinkedListDeque.java
- Wrote the iterator(), equals(), and toString() methods

# MaxArrayDeque.java
- Create the MaxArrayDeque Class
- The MaxArrayDeque is similar to the ArrayDeque, with the addition of two methods and a constructor
  - public MaxArrayDeque(Comparator<T> c): creates a MaxArrayDeque with the given comparator
  - public T max(): returns the maximum element in the Deque given by the previous comparator
  - public T max(Comparator<T> c): returns the maximum element in the Deque given by the given comparator
