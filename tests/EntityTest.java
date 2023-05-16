import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rpg.*;
import rpg.exceptions.DeadEntityException;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private static Hero hero;
    private static Hero deadHero;

    private static Monster monster;
    private static Monster weakMonster;
    private static Monster deadMonster;

    private static Weapon weapon;
    private static Weapon weapon2;
    private static Armor armor;
    private static Backpack backpack;
    private static Purse purse;


    @BeforeEach
    void setup() {

        weapon = new Weapon(1.3, 21);
        weapon2 = new Weapon(0.7, 49);
        purse = new Purse(0.03, 200);
        armor = new Armor(0L, 12.4, 200, 45);
        backpack = new Backpack(0.75, 75, 40.5);

        Collection<Item> heroItems = new HashSet<>(){{
            add(weapon);
            add(purse);
        }};

        hero = new Hero("Human: World's Hero", 101, 97, heroItems, 25.30);
        deadHero = new Hero("Here lies", 23.21);
        deadHero.die();

        Collection<Item> monsterItems = new HashSet<>(){{
            add(armor);
            add(backpack);
        }};

        monster = new Monster("Goblin", 130, 10, 49, monsterItems);
        weakMonster = new Monster("Killable", 2, 1, 7, new HashSet<>(){{add(weapon2);}});
        deadMonster = new Monster("Corpse", 100, 1, 7);
        deadMonster.die();

    }

    @Test
    void hit_Legal() {
        int heroHP = hero.getHitPoints();
        int heroDMG = hero.getDamage();
        int monsterHP = monster.getHitPoints();
        int weakMonsterHP = weakMonster.getHitPoints();

        try {
            hero.hit(monster);
            hero.hit(weakMonster);
        } catch (Exception e) {
            fail();
        }
        assertTrue(monster.getHitPoints() == monsterHP || monster.getHitPoints() == monsterHP-heroDMG);
        assertTrue(hero.canHaveAsHitPoints(hero.getHitPoints()));
        assertTrue(monster.canHaveAsHitPoints(monster.getHitPoints()));
        assertTrue(weakMonster.isDead() || weakMonster.getHitPoints() == weakMonsterHP);
        if(weakMonster.isDead()) {
            assertTrue(weapon2.getHolder() == hero || (weapon2.isBroken() && weapon2.liesOnGround()));
            assertEquals(0, weakMonster.getHitPoints());
        }



    }

    @Test
    void hit_Illegal() {
        assertThrows(DeadEntityException.class, () -> hero.hit(deadMonster));
        assertThrows(DeadEntityException.class, () -> deadHero.hit(monster));
        assertThrows(DeadEntityException.class, () -> monster.hit(deadHero));
        assertThrows(DeadEntityException.class, () -> deadMonster.hit(hero));
        assertThrows(IllegalArgumentException.class, () -> hero.hit(null));
        assertThrows(IllegalArgumentException.class, () -> monster.hit(null));

    }

    @Test
    void drop_Legal() {
        hero.drop(weapon);
        assertFalse(hero.holdsItemDirectly(weapon));
        hero.drop(purse);
        assertFalse(hero.holdsItemDirectly(purse));

        monster.drop(armor);
        assertFalse(monster.holdsItemDirectly(armor));
        monster.drop(backpack);
        assertFalse(monster.holdsItemDirectly(backpack));

    }

    @Test
    void drop_Illegal() {
        //
        assertThrows(IllegalArgumentException.class, () -> hero.drop(null));
        assertThrows(IllegalArgumentException.class, () -> hero.drop(armor));
        assertThrows(IllegalArgumentException.class, () -> hero.drop(weapon2));
        assertThrows(IllegalArgumentException.class, () -> hero.drop(backpack));
    }
}
