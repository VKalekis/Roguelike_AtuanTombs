package Game.Map;

import java.util.*;
import java.util.stream.Collectors;

public class Labyrinth {

//    public static void main(String[] args) {
//
//        Scanner input = new Scanner(System.in);
//        int steps = 0;
//
//        Room currentRoom = initializeLabyrinth();
//        Room nextRoom;
//        String nextRoomName;
//
//
//        while (!currentRoom.isFinalRoom()) {
//
//
//            System.out.println("Current Room : " + currentRoom.toString());
//            System.out.println("Available Rooms : " + currentRoom.getConnectedRoomsMap());
//
//
//
//            nextRoomName = input.nextLine();
//            nextRoom = currentRoom.goTo(nextRoomName);
//
//            if (nextRoom == null) {
//                System.out.println("You chose a wrong value, choose a passage from the list.");
//            } else {
//                System.out.println("Next Room : " + nextRoom.toString());
//                System.out.println("--------------------------------------------");
//
//                currentRoom = nextRoom;
//                steps++;
//
//                if (currentRoom.isFinalRoom()) {
//                    System.out.println("Hooray, you made it out and it only took you " + steps + " steps");
//                }
//            }
//
//        }
//
////    }




    public static Room initializeLabyrinth() {

        Random random = new Random();
        List<RoomMap> roomMapArrayList = new ArrayList<>();

        while(roomMapArrayList.size()<9){
            RoomMap roomMap = new RoomMap();
            roomMap.initializeMap();
            roomMap.generateRandomMap(50-random.nextInt(10));
            roomMap.cellularAutomata();
            //map.cellularAutomata();
            roomMap.floodFill();
            if (roomMap.getFillRate()>0.3){
                roomMapArrayList.add(roomMap);
            }
        }
        System.out.println(roomMapArrayList);

        System.out.println("Sorted Fillrates");
        roomMapArrayList.stream().map((m)->m.getFillRate()).sorted().forEach(System.out::println);


        roomMapArrayList = roomMapArrayList.stream().sorted((o1, o2) -> Double.compare(o1.getFillRate(), o2.getFillRate())).collect(Collectors.toList());
        System.out.println(roomMapArrayList);
        System.out.println(roomMapArrayList.get(0).getFillRate());
        System.out.println(roomMapArrayList.get(8).getFillRate());
        System.out.println(roomMapArrayList.stream().map((m)->m.getFillRate()).mapToDouble(Double::doubleValue).summaryStatistics());

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
        Room theUndergroundGarden = new Room("The underground garden",
                "A Garden of Wanders in a place so dark.",
                roomMapArrayList.get(6));
        Room theUndertomb = new Room("The Undertomb",
                "The Nameless Ones writhe in their sleep.",
                roomMapArrayList.get(7));
        Room theHallOfThrone = new Room("The Hall of Throne",
                "The Great Hall of Throne.",
                roomMapArrayList.get(8), true);

        thePit.addEntrance();
        thePit.addConnectedRoom(theRoomOfChains);
        thePit.addConnectedRoom(theRoomOfBones);
        thePit.setTextures();

        theRoomOfChains.addEntrance();
        theRoomOfChains.addConnectedRoom(theRoomOfBones);
        theRoomOfChains.setTextures();

        theRoomOfBones.addEntrance();
        theRoomOfBones.addConnectedRoom(thePit);
        theRoomOfBones.addConnectedRoom(thePaintedRoom);
        theRoomOfBones.setTextures();

        thePaintedRoom.addEntrance();
        thePaintedRoom.addConnectedRoom(theTreasureRoom);
        thePaintedRoom.setTextures();

        theTreasureRoom.addEntrance();
        theTreasureRoom.addConnectedRoom(thePit);
        theTreasureRoom.addConnectedRoom(theUndertomb);
        theTreasureRoom.setTextures();

        theUndertomb.addEntrance();
        theUndertomb.addConnectedRoom(thePaintedRoom);
        theUndertomb.addConnectedRoom(thePit);
        theUndertomb.addConnectedRoom(theRedRockRoom);
        theUndertomb.addConnectedRoom(theUndergroundGarden);
        theUndertomb.setTextures();

        theRedRockRoom.addEntrance();
        theRedRockRoom.addConnectedRoom(thePit);
        theRedRockRoom.setTextures();

        theUndergroundGarden.addEntrance();
        theUndergroundGarden.addConnectedRoom(theUndertomb);
        theUndergroundGarden.addConnectedRoom(theHallOfThrone);
        theUndergroundGarden.setTextures();

        theHallOfThrone.addEntrance();
        theHallOfThrone.addConnectedRoom(thePit);
        theHallOfThrone.setTextures();


        return thePit;
    }

}
