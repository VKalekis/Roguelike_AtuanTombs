package Game.Map;


import Game.Items.*;
import Game.PlayerSrc.AbstractPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class Room {

    private final String name;
    private final String description;
    private final RoomMap roomMap;
    private boolean finalRoom;
    private Map<Position, Room> connectedRoomsMap;
    private Position entrance;

    private UsableFactory usableFactory;
    private Map<Position, UsableItem> usablesMap;
    private EquippableFactory equippableFactory;
    private Map<Position, EquippableItem> equippablesMap;

    public Room(String name, String description, RoomMap roomMap) {
        this.name = name;
        this.description = description;
        this.roomMap = roomMap;
        this.connectedRoomsMap = new HashMap<>();
        this.finalRoom = false;

        this.usableFactory = new UsableFactory();
        this.usablesMap = new HashMap<>();
        this.equippableFactory = new EquippableFactory();
        this.equippablesMap = new HashMap<>();

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

    public RoomMap getRoomMap() {
        return roomMap;
    }

    public Position getEntrance() {
        return entrance;
    }

    public List<Room> getConnectedRooms() {
        return connectedRoomsMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Room: " + name + ", description : " + description;
    }

    public Room getConnectedRoom(Position position) {
        return connectedRoomsMap.get(position);
    }

    public boolean isExit(Position position) {
        return connectedRoomsMap.containsKey(position);
    }

    public void generateUsables(AbstractPlayer player) {
        usablesMap.clear();

        for (int i=0;i<5;i++) {
            Position randomPosition = roomMap.getRandomPosition(MapTileType.FLOOR);
            usablesMap.put(randomPosition, usableFactory.makeUsable(player.getClass(), randomPosition));
        }
    }

    public Map<Position, UsableItem> getUsablesMap() {
        return usablesMap;
    }

    public void generateEquippables(AbstractPlayer player) {
        equippablesMap.clear();

        for (int i=0;i<4;i++) {
            Position randomPosition = roomMap.getRandomPosition(MapTileType.FLOOR);
            equippablesMap.put(randomPosition, equippableFactory.makeEquippable(player,randomPosition));
        }
    }

    public Map<Position, EquippableItem> getEquippablesMap() {
        return equippablesMap;
    }
}
