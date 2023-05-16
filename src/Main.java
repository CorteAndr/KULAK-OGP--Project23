import rpg.Entity;
import rpg.Hero;
import rpg.Monster;
import rpg.Weapon;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.DeadEntityException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InvalidHolderException, BrokenItemException, DeadEntityException {
        Hero hero = new Hero("Saver O'Humanity", 213, Entity.getFirstLowerPrime(213), 25.00);
        hero.pickup(new Weapon(13.00, 21));

        Monster monster = new Monster("Calamity", 250, 20, 15);

        System.out.printf("%s its possessions are worth %d\n\n",hero,hero.getValueHeld());
        System.out.printf("%s its possessions are worth %d\n\n", monster, monster.getValueHeld());

        if((new Random()).nextBoolean()) {
            hero.fight(monster);
        } else {
            monster.fight(hero);
        }
        if(monster.isDead()) {
            System.out.printf("The winner is %s his possessions are now worth %d", hero, hero.getValueHeld());
        } else {
            System.out.printf("The winner is %s his possessions are now worth %d", monster, monster.getValueHeld());
        }
    }
}
