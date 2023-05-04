package rpg;

import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.Map;
import java.util.Random;

//TODO Everything
public class Monster extends Entity {

    protected Monster(String name, int maxHitPoints, int protection) throws IllegalArgumentException, InvalidAnchorException {
        super(name, maxHitPoints, protection);
    }

    protected Monster(String name, int maxHitPoints, int hitPoints, int protection) throws IllegalArgumentException, InvalidAnchorException {
        super(name, maxHitPoints, hitPoints, protection);
    }

    protected Monster(String name, int maxHitPoints, Map<Anchor, Item> anchors, int protection) throws IllegalArgumentException, InvalidAnchorException, BrokenItemException, InvalidHolderException {
        super(name, maxHitPoints, anchors, protection);
    }

    protected Monster(String name, int maxHitPoints, int hitPoints, Map<Anchor, Item> anchors, int protection) throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException, BrokenItemException {
        super(name, maxHitPoints, hitPoints, anchors, protection);
    }

    @Override
    public double getCapacity() {
        return 0;
    }

    @Override
    protected int getDefaultProtection() {
        return 0;
    }

    @Override
    protected int getHitChance() {
        return (new Random()).nextInt(0, Math.min(100, getHitPoints()) + 1);
    }

    @Override
    protected int getDamage() {
        return 0;
    }

    @Override
    protected int getProtection() {
        return 0;
    }

    @Override
    protected void collectTreasuresFrom(Entity opponent) throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException {

    }
}
