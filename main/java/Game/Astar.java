package Game;


import Game.Map.MapTile;
import Game.Map.MapTileType;
import Game.Map.Position;
import Game.Map.RoomMap;

import java.util.*;

public class Astar {
    RoomMap roomMap;
    MapTile start, goal;

    //https://www.redblobgames.com/pathfinding/a-star/introduction.html
    //https://www.redblobgames.com/pathfinding/a-star/implementation.html
    public Astar() {
    }

    public int heuristic(MapTile mapTile1, MapTile mapTile2) {
        return Math.abs(mapTile1.getPosition().getI() - mapTile2.getPosition().getI()) +
                Math.abs(mapTile1.getPosition().getJ() - mapTile2.getPosition().getJ());
    }

    public List<Position> run(RoomMap roomMap, MapTile start, MapTile goal) {
        this.roomMap = roomMap;
        this.start = start;
        this.goal = goal;
        System.out.println("Start"+start.getPosition());
        System.out.println("Goal"+goal.getPosition());

        PriorityQueue<MapTile> priorityQueue = new PriorityQueue<>(new Comparator<MapTile>() {
            @Override
            public int compare(MapTile o1, MapTile o2) {
                return Integer.compare(o1.getCost(), o2.getCost());
            }
        });

        Map<MapTile, Integer> costs = new HashMap<>();
        Map<MapTile, MapTile> paths = new HashMap<>();

        priorityQueue.add(start);
        costs.put(start, 0);
        paths.put(start, start);

        MapTile currentMapTile;
        int newNeighbourCost;

        while (!priorityQueue.isEmpty()) {
            currentMapTile = priorityQueue.poll();
            //System.out.println("Polled element" + currentMapTile.getPosition());
            if (currentMapTile.equals(goal)) {
                break;
            }

            List<MapTile> neighbours = roomMap.getSurroundingCardinal(
                    currentMapTile.getPosition().getI(), currentMapTile.getPosition().getJ(), MapTileType.FLOOR);
            neighbours.addAll(roomMap.getSurroundingCardinal(
                    currentMapTile.getPosition().getI(), currentMapTile.getPosition().getJ(), MapTileType.ENTRANCE));
            neighbours.addAll(roomMap.getSurroundingCardinal(
                    currentMapTile.getPosition().getI(), currentMapTile.getPosition().getJ(), MapTileType.EXIT));

            for (MapTile neighbour : neighbours) {
                newNeighbourCost = costs.get(currentMapTile) + 1; //not weighted graph, all are 1.

                if ((!costs.containsKey(neighbour)) || (newNeighbourCost < costs.get(neighbour))) {
                    //System.out.println("List put" + neighbour.getPosition());
                    costs.put(neighbour,newNeighbourCost);
                    neighbour.setCost(newNeighbourCost+heuristic(neighbour,goal));
                    priorityQueue.add(neighbour);
                    paths.put(neighbour,currentMapTile);
                }
            }
        }
        System.out.println("Testin"+paths.size());
        return reconstructPath(paths);

    }

    private List<Position> reconstructPath(Map<MapTile,MapTile> paths) {
        List<MapTile> finalPath = new LinkedList<>();
        List<Position> nextMoves = new LinkedList<>();

        finalPath.add(goal);

        MapTile current = goal;
        while (current!=start) {

            current=paths.get(current);
            finalPath.add(current);
        }

        Collections.reverse(finalPath);

        System.out.println("Ended:Printing");
        for (MapTile tile : finalPath) {
//            tile.deleteTexture();
//            tile.addTextureName("ASTAR.png");
            System.out.println(tile.getPosition());
            nextMoves.add(tile.getPosition());
        }
        return nextMoves;
    }

    public static void main(String[] args) {
        PriorityQueue<MapTile> priorityQueue = new PriorityQueue<>(new Comparator<MapTile>() {
            @Override
            public int compare(MapTile o1, MapTile o2) {
                return Integer.compare(o1.getCost(), o2.getCost());
            }
        });

        priorityQueue.add(new MapTile(MapTileType.ENTRANCE, new Position()));
        MapTile test = new MapTile(MapTileType.ENTRANCE, new Position());
        test.setCost(1);
        MapTile test1 = new MapTile(MapTileType.ENTRANCE, new Position());
        test1.setCost(2);
        priorityQueue.add(test1);
        priorityQueue.add(test);
        System.out.println(priorityQueue.poll().getCost());
        System.out.println(priorityQueue.poll().getCost());

    }
}
