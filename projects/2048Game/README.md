# 2048 Game

## Goal of Project:

- Write the core logic behind the 2048 Game: 1) What happens when there are empty spaces around tiles 2) What happens when two of the same-number tiles are adjacent to each other 3) What happens to surrounding tiles when you move one tile either up, down, left, or to the right

## Files to Look At + My Task:

# Model.java

- emptySpaceExists(): returns True if any of the given tiles on the board are null
- maxTileExists(): returns True if any of the tiles in the board are equal to the winning value of 2048
- atLeastOneMoveExists(): returns True if there is a valid move, with a valid move meaning that if the user presses either UP, DOWN, LEFT, or RIGHT atleast one tile will move 
- tilt(): the main logic of the game
