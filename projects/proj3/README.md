# BYOW: Game

## Goal of Project:

- Use software engineering to create an explorable word with an avatar
- A new, randomly-sized world is generated each time the game is run
- An avatar can move across the board with WASD commands

## Files to Look At + My Task:

### Core Folder

- Avatar.java: Creates an Avatar object with X, Y, width, and height variables
- Coordinate.java: Represents the X and Y coordinates for each Room
- Room.java: Represents a Room object with the given Coordinate
- World.java: Creates a world with multiple Rooms, with hallways connecting each Room, and a wall surrounding the Rooms and hallways
- Main.java: Calls the World class, passing in the random seed for the room and hallway generation 
