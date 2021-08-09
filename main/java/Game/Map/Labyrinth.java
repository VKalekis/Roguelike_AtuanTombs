package Game.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Labyrinth {

    public static Room initializeLabyrinth() {

        Random random = new Random();
        List<RoomMap> roomMapArrayList = new ArrayList<>();

        while (roomMapArrayList.size() < 9) {
            RoomMap roomMap = new RoomMap();
            roomMap.initializeMap();
            roomMap.generateRandomMap(50 - random.nextInt(10));
            roomMap.cellularAutomata();
            roomMap.floodFill();

            if (roomMap.getFillRate() > 0.3) {
                roomMapArrayList.add(roomMap);
            }
        }
        System.out.println(roomMapArrayList);

        System.out.println("Sorted Fillrates");
        roomMapArrayList.stream().map((m) -> m.getFillRate()).sorted().forEach(System.out::println);


        roomMapArrayList = roomMapArrayList.stream().sorted((o1, o2) -> Double.compare(o1.getFillRate(), o2.getFillRate())).collect(Collectors.toList());
        System.out.println("Sorted Rooms:");
        System.out.println(roomMapArrayList);
        System.out.println("Statistics from map generation:");
        System.out.println(roomMapArrayList.stream().map((m) -> m.getFillRate()).mapToDouble(Double::doubleValue).summaryStatistics());

        Room thePit = new Room("The Pit",
                "A dark pit, where the voices of the damned echo.",
                roomMapArrayList.get(0));
        Room theRoomOfChains = new Room("The Room of Chains",
                "Chains rustle all around you.",
                roomMapArrayList.get(1));
        Room thePaintedRoom = new Room("The Painted Room",
                "A faint light reveals walls painted with winged creatures.",
                roomMapArrayList.get(2));
        Room theTreasureRoom = new Room("The Treasure Room",
                "Little lights sparkle in the darkness.",
                roomMapArrayList.get(3));
        Room theRoomOfBones = new Room("The Room of Bones",
                "Bones rattle in the darkness.",
                roomMapArrayList.get(4));
        Room theRedRockRoom = new Room("The Red Rock Room",
                "Noone has crossed the Red Rock Door, ever.",
                roomMapArrayList.get(5));
        Room theUndergroundGarden = new Room("The Underground Garden",
                "A Garden of Wanders in a place so dark.",
                roomMapArrayList.get(6));
        Room theUndertomb = new Room("The Undertomb",
                "The Demons writhe in their sleep.",
                roomMapArrayList.get(7));
        Room theHallOfThrone = new Room("The Hall of Throne",
                "The Great Hall of Throne.",
                roomMapArrayList.get(8), true);
        Room dummyFinalRoom = new Room("The End!",
                "The end", null);

        thePit.addEntrance();
        thePit.addConnectedRoom(theRoomOfChains);
        thePit.addConnectedRoom(theRoomOfBones);
        thePit.setSprites();

        theRoomOfChains.addEntrance();
        theRoomOfChains.addConnectedRoom(theRoomOfBones);
        theRoomOfChains.setSprites();

        theRoomOfBones.addEntrance();
        theRoomOfBones.addConnectedRoom(thePit);
        theRoomOfBones.addConnectedRoom(thePaintedRoom);
        theRoomOfBones.setSprites();

        thePaintedRoom.addEntrance();
        thePaintedRoom.addConnectedRoom(theTreasureRoom);
        thePaintedRoom.setSprites();

        theTreasureRoom.addEntrance();
        theTreasureRoom.addConnectedRoom(thePit);
        theTreasureRoom.addConnectedRoom(theUndertomb);
        theTreasureRoom.setSprites();

        theUndertomb.addEntrance();
        theUndertomb.addConnectedRoom(thePaintedRoom);
        theUndertomb.addConnectedRoom(thePit);
        theUndertomb.addConnectedRoom(theRedRockRoom);
        theUndertomb.addConnectedRoom(theUndergroundGarden);
        theUndertomb.setSprites();

        theRedRockRoom.addEntrance();
        theRedRockRoom.addConnectedRoom(thePit);
        theRedRockRoom.setSprites();

        theUndergroundGarden.addEntrance();
        theUndergroundGarden.addConnectedRoom(theUndertomb);
        theUndergroundGarden.addConnectedRoom(theHallOfThrone);
        theUndergroundGarden.setSprites();

        theHallOfThrone.addEntrance();
        theHallOfThrone.addConnectedRoom(dummyFinalRoom);
        theHallOfThrone.setSprites();

        return thePit;
    }
}
