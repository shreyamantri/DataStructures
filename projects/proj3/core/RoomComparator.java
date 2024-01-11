package core;

import java.util.Comparator;

public class RoomComparator implements Comparator<Room> {
    public int compare(Room room1, Room room2) {
        return Integer.compare(room1.getX(), room2.getX());
    }
}
