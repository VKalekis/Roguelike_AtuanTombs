package Game.Map;


import java.util.*;
import java.util.stream.Collectors;

public class Room {

    private final String name;
    private final String description;
    private final RoomMap roomMap;
    private Map<Position, Room> connectedRooms;

    private boolean finalRoom;

    public Room(String name, String description, RoomMap roomMap) {
        this.name = name;
        this.description = description;
        this.roomMap = roomMap;
        this.connectedRooms = new HashMap<>();
        this.finalRoom = false;
    }

    public Room(String name, String description, RoomMap roomMap, boolean finalRoom) {
        this(name, description, roomMap);
        this.finalRoom = finalRoom;
    }

    public boolean isFinalRoom() {
        return finalRoom;
    }

    public void addConnectedRoom(Room connectedRoom) {
        Position pos = roomMap.addExit();
        System.out.println(pos+"///");
        this.connectedRooms.put(pos, connectedRoom);
        System.out.println(connectedRooms);
    }

    public Room goTo(String name) {
        Optional<Room> optionalRoom = this.connectedRooms.values().stream().filter((x)-> x.getName().equals(name)).findFirst();
        return optionalRoom.orElse(null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RoomMap getMap() {
        return roomMap;
    }

    public List<Room> getConnectedRooms11() {
        return connectedRooms.values().stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Room: " + name + ", description : " + description;
    }

    public Room getFromMap(Position position) {
        return connectedRooms.get(position);
    }

    public Map<Position, Room> getConnectedRooms() {
        return connectedRooms;
    }
}
