package Game.Enemies;

import Game.Map.MapTile;
import Game.Map.MapTileType;
import Game.Map.Position;
import Game.Map.RoomMap;

import java.util.*;

public class Astar {
    private MapTile start, goal;

    /* Implementation of A* pathfinding algorithm from maptile start -> maptile goal,
       using the roomMap of the current room.
       https://www.redblobgames.com/pathfinding/a-star/introduction.html
       https://www.redblobgames.com/pathfinding/a-star/implementation.html
     */
    public Astar() {
    }

    private int heuristic(MapTile mapTile1, MapTile mapTile2) {
        return Math.abs(mapTile1.getPosition().getI() - mapTile2.getPosition().getI()) +
                Math.abs(mapTile1.getPosition().getJ() - mapTile2.getPosition().getJ());
    }

    public List<Position> run(RoomMap roomMap, MapTile start, MapTile goal) {

        this.start = start;
        this.goal = goal;

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

            if (currentMapTile.equals(goal)) {
                break;
            }

            List<MapTile> neighbours = roomMap.getSurroundingCardinal(
                    currentMapTile.getPosition().getI(),
                    currentMapTile.getPosition().getJ(),
                    Arrays.asList(MapTileType.FLOOR, MapTileType.ENTRANCE, MapTileType.EXIT));

            for (MapTile neighbour : neighbours) {
                newNeighbourCost = costs.get(currentMapTile) + 1; //not weighted graph, all are 1.

                if ((!costs.containsKey(neighbour)) || (newNeighbourCost < costs.get(neighbour))) {
                    costs.put(neighbour, newNeighbourCost);
                    neighbour.setCost(newNeighbourCost + heuristic(neighbour, goal));
                    priorityQueue.add(neighbour);
                    paths.put(neighbour, currentMapTile);
                }
            }
        }
        return reconstructPath(paths);

    }

    private List<Position> reconstructPath(Map<MapTile, MapTile> paths) {
        List<MapTile> finalPath = new LinkedList<>();
        List<Position> nextMoves = new LinkedList<>();

        finalPath.add(goal);

        MapTile current = goal;
        while (current != start) {
            current = paths.get(current);
            finalPath.add(current);
        }

        Collections.reverse(finalPath);

        for (MapTile tile : finalPath) {
            nextMoves.add(tile.getPosition());
        }

        return nextMoves;
    }


}
