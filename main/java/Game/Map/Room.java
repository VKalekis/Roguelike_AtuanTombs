package Game.Map;


import java.util.*;
import java.util.stream.Collectors;

public class Room {

    private final String name;
    private final String description;
    private final RoomMap roomMap;
    private Map<Position, Room> connectedRoomsMap;
    private Position entrance;

    private boolean finalRoom;

    public Room(String name, String description, RoomMap roomMap) {
        this.name = name;
        this.description = description;
        this.roomMap = roomMap;
        this.connectedRoomsMap = new HashMap<>();
        this.finalRoom = false;
    }

    public Room(String name, String description, RoomMap roomMap, boolean finalRoom) {
        this(name, description, roomMap);
        this.finalRoom = finalRoom;
    }

    public boolean isFinalRoom() {
        return finalRoom;
    }

    public void addEntrance() {
        entrance = roomMap.addEntrance();
    }

    public void addConnectedRoom(Room connectedRoom) {
        Position pos = roomMap.addExit();
        System.out.println(pos+"///");
        this.connectedRoomsMap.put(pos, connectedRoom);
        System.out.println(connectedRoomsMap);
    }

    public void setTextures() {
        roomMap.setFloorsTextures();
        roomMap.setWallsTextures();

    }

    public Room goTo(Position position) {
//        Optional<Room> optionalRoom = this.connectedRoomsMap.values().stream().filter((x)-> x.getName().equals(name)).findFirst();
//        return optionalRoom.orElse(null);
        return connectedRoomsMap.get(position);
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

    public Position getEntrance() {
        return entrance;
    }

    public List<Room> getConnectedRooms11() {
        return connectedRoomsMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Room: " + name + ", description : " + description;
    }

    public Room getFromMap(Position position) {
        return connectedRoomsMap.get(position);
    }

    public boolean isExit(Position position) {
        return connectedRoomsMap.containsKey(position);
    }
}
