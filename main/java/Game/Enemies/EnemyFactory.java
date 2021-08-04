package Game.Enemies;

import Game.PlayerSrc.AbstractPlayer;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyFactory {

    private final AbstractPlayer player;
    Map<Integer, List<Class>> levelEnemyMap;
    private final Random random;

    public EnemyFactory(AbstractPlayer player) {
        this.player = player;
        this.levelEnemyMap = Map.of(1, List.of(Goblin.class),
                2, List.of(Goblin.class, IceZombie.class),
                3, List.of(Goblin.class, IceZombie.class, Swampy.class),
                4, List.of(IceZombie.class, Swampy.class, OrcShaman.class),
                5, List.of(Swampy.class, OrcShaman.class),
                6, List.of(Swampy.class, OrcShaman.class, Necromancer.class));

        this.random = new Random();
    }


    public Enemy makeEnemy() {
        List<Class> possibleEnemies = levelEnemyMap.get(player.getLevel());
        Class enemyClass = possibleEnemies.get(random.nextInt(possibleEnemies.size()));

        try {
            Enemy enemy = (Enemy) enemyClass.getDeclaredConstructor().newInstance();
            return enemy;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
