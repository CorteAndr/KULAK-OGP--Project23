import rpg.Hero;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.HashMap;
import java.util.Random;
import rpg.*;

public class Main {
    public static void main(String[] args) throws InvalidHolderException, BrokenItemException, InvalidAnchorException {
        Hero hero = new Hero("Saver O'Humanity", 213, Entity.getFirstLowerPrime(213),
                new HashMap<Anchorpoint, Item>(){{
                    put(Anchorpoint.LEFT_HAND, new Weapon(10, 30));
    }},25.00);

        Monster monster = new Monster("Calamity", 183, Entity.getFirstLowerPrime(183),
        new HashMap<Anchorpoint, Item>() {{
        put(Anchorpoint.RIGHT_HAND, new Backpack(25, 100,600));
        }}, 60, 20);

        System.out.printf("%s possessions are worth %d\n",hero,hero.getValueHeld());
        System.out.printf("%s possessions are worth %d\n", monster, monster.getValueHeld());

        if((new Random()).nextBoolean()) {
            hero.fight(monster);
        } else {
            monster.fight(hero);
        }
        if(monster.isDead()) {
            System.out.printf("The winner is %s, his possessions are now worth %d", hero, hero.getValueHeld());
        } else {
            System.out.printf("The winner is %s, his possessions are now worth %d", monster, monster.getValueHeld());
        }
    }
}
