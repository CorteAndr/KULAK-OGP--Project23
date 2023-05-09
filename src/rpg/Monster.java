package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.DeadEntityException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.*;


/**
 * A class of monster
 *
 * @author  Corteville Andrew
 *
 * @invar   Each monster must have a valid effective protection
 *          | canHaveAsEffectiveProtection(getProtection())
 * @invar   Each monster mush have a valid damage
 *          | isValidDamage(getDamage())
 *
 */
public class Monster extends Entity {


    /**
     * Initializes this monster with the given name, maximum hit points, hit points, anchors and protection.
     *
     * @param   name
     *          The name for the new monster
     * @param   maxHitPoints
     *          The maximum hit points of the new monster
     * @param   hitPoints
     *          The actual hit points of the new monster
     * @param   anchors
     *          The anchors of the new monster
     * @param   protection
     *          The protection of the new monster
     *
     * @effect  Initializes this monster with the given name, maximum hit points, actual hit points and protection
     *          | super(name, maxHitPoints, hitPoints, protection)
     * @effect  The anchors of this monster are set to the given anchors
     *          | setAnchors(anchors)
     * @post    The capacity of this new monster is set to the load of its anchors
     *          | new.getCapacity() == getLoad()
     */
    @Raw
    public Monster(String name, int maxHitPoints, int hitPoints, Map<Anchorpoint, Item> anchors, int protection, int damage)
            throws InvalidAnchorException, InvalidHolderException {
        super(name, maxHitPoints, hitPoints, protection);
        setDamage(damage);
        double capacity = 0;
        for (Item item : anchors.values()) {
            if (item != null) capacity += item.getWeight();
        }
        this.capacity = capacity;
        setAnchors(anchors);
    }

    /**
     * Initializes this new monster with the given name, maximum hit points, protection and damage.
     *
     * @param   name
     *          The name for the monster
     * @param   maxHitPoints
     *          The maximum hit points of the new monster
     * @param   protection
     *          The maximum protection of the monster
     * @param   damage
     *          The damage of the new monster
     *
     * @effect  Initializes this new monster with the given name, maximum hit points and protection
     *          | super(name, maxHitPoints, protection)
     * @effect  The damage of this new monster is set to the given damage
     *          | setDamage(damage)
     * @effect  The anchors of this new monster are set to the randomly generated anchors
     *          | setAnchors(getRandomAnchors())
     * @post    The capacity of this monster is set to 10.00 (kg) multiplied by the number of anchors
     *          this monster has
     *          | new.getCapacity() == 10.00 * getAnchorsPoints().size();
     */
    @Raw
    public Monster(String name, int maxHitPoints, int protection, int damage)
            throws InvalidAnchorException, InvalidHolderException, BrokenItemException {
        super(name, maxHitPoints, protection);
        setDamage(damage);
        setAnchors(getRandomAnchors());
        this.capacity = 10.00 * getAnchorPoints().size();
    }

    /**
     * Initializes this monster with the given name, maximum hit points, protection and damage and a list of items that
     * are attempted to add to this monster
     *
     * @param   name
     *          The name for the Monster
     * @param   maxHitPoints
     *          The maximum hit points of the new Monster
     * @param   protection
     *          The protection of the new Monster
     * @param   damage
     *          The damage of the new Monster
     * @param   items
     *          The items to allocate
     *
     * @effect  Initializes this new Monster with the given name, maximum hit points and protection.
     *          | super(name, maxHitPoints, protection)
     * @effect  The damage of this new Monster is set to the given damage
     *          | setDamage(damage)
     * @effect  The anchors are set to the anchors which contains the most possible of the given items
     *          | setAnchors(getInitializedAnchors(items))
     * @post    The capacity of this new Monster is set load of the previously created anchors
     *          | new.getCapacity() == getLoad()
     */
    @Raw
    public Monster(String name, int maxHitPoints, int protection, int damage, List<Item> items)
            throws InvalidAnchorException, InvalidHolderException {
        super(name, maxHitPoints, protection);
        setDamage(damage);
        setAnchors(getInitializedAnchors(items));
        this.capacity = getLoad();
    }

    /**
     * Generates a map of anchors that contains the maximum number of the given items
     *
     * @param   items
     *          The items to allocate
     * @return
     *          | result == new HashMap<>() {{
     *          | for each item in items:
     *          |   for each anchor in AnchorPoints.values():
     *          |       if( !result.containsKey(anchor) && anchor.canHoldItem(item) )
     *          |       then put(anchor, item)
     *          | }}
     */
    //TODO ?
    @Raw
    private static Map<Anchorpoint, Item> getInitializedAnchors(List<Item> items) {
        Map<Anchorpoint, Item> result = new HashMap<>();
        for(Item item: items) {
            for(Anchorpoint anchor: Anchorpoint.values()) {
                if(!result.containsKey(anchor) && anchor.canHoldItem(item)) {
                    result.put(anchor, item);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Generates a random map of anchors with random items
     *
     * @return  A map of random amount of anchors with no linked items
     *          | result == Map<Anchorpoint, Item>().
     */
    //TODO ?
    private static Map<Anchorpoint, Item> getRandomAnchors()
            throws BrokenItemException, InvalidAnchorException, InvalidHolderException {
        Map<Anchorpoint, Item> result = new HashMap<>();
        for(Anchorpoint anchor: Anchorpoint.values()) {
            if ((new Random()).nextBoolean()) {
                if (anchor.equals(Anchorpoint.BELT))
                    result.put(anchor, new Purse(25.00, 100));
                else {
                    result.put(anchor, switch ((new Random()).nextInt(0, 3)) {
                        case 0 -> new Weapon(4.00, 25);
                        case 1 -> new Armor(-1, 16.00, 103, 13);
                        case 2 -> new Backpack(10, 50, 40.50);
                        default -> throw new IllegalStateException("Unexpected value");
                    });
                }
            }
        }
        return result;
    }

    /**
     * Variable referencing the maximum weight this monster can hold.
     */
    private final double capacity;

    /**
     * Returns the maximum weight this monster can hold.
     */
    @Override
    @Basic @Immutable
    public double getCapacity() {
        return capacity;
    }

    /**
     * Returns the default protection of a monster
     */
    @Override
    public int getDefaultProtection() {
        return 5;
    }

    /**
     * Returns the rolled hit value to compare to an opponents protection
     *
     * @return  A randomly generated between
     */
    @Override
    protected int getHitChance() {
        return (new Random()).nextInt(0, Math.min(100, getHitPoints()) + 1);
    }

    /**
     * Variable referencing the damage of this entity.
     */
    private int damage = 0;

    /**
     * Returns the damage of this monster
     */
    @Override
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage of this entity to the given damage
     *
     * @pre     The given damage is valid damage for a monster
     *          | isValidDamage(damage)
     *
     * @post    The damage of this monster is set to the given damage
     */
    @Raw
    private void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Checks if the given damage is valid for a monster
     *
     * @param   damage
     *          The damage to check
     *
     * @return  True if and only if the given damage is valid for a monster/weapon
     *          | result == Weapon.isValidDamage(damage)
     */
    public static boolean isValidDamage(int damage) {
        return Weapon.isValidDamage(damage);
    }

    /**
     * Variable referencing the effective protection this monster has
     */
    private int effectiveProtection = 0;

    /**
     * Checks if the given protection is valid for this monster
     *
     * @param   protection
     *          The protection to check.
     *
     * @return  True if and only if the given protection is an integer between 1 and
     *          the intrinsic protection of this monster
     *          | result == (
     *          |   (1 <= protection) &&
     *          |   (protection <= getBaseProtection())
     *          | )
     */
    public boolean canHaveAsEffectiveProtection(int protection) {
        return (1 <= protection && protection <= getBaseProtection());
    }

    /**
     * Sets the effective protection of this monster to the given protection
     *
     * @param   protection
     *          The new protection of the given
     */
    private void setEffectiveProtection(int protection) {
        if(!canHaveAsEffectiveProtection(protection))
            throw new IllegalArgumentException(this + " cannot have " + protection + " as protection.");
        this.effectiveProtection = protection;
    }

    /**
     * Returns the protection this monster has
     */
    @Override
    public int getProtection() {
        return effectiveProtection;
    }

    /**
     * Collects items from the
     * @param   opponent
     *          The opponent to collect treasures from
     */
    @Override
    @Raw
    protected void collectTreasuresFrom(Entity opponent) throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException {
        for (Anchorpoint anchorOpp: opponent.getAnchorPoints()) {
            Item item = opponent.getItemAt(anchorOpp);
            if(canPickup(item)) {
                for (Anchorpoint anchor: getAnchorPoints()) {
                    Item curItem = getItemAt(anchor);
                    if ((curItem == null || curItem.getShiny() < item.getShiny()) && canHaveItemAtAnchor(item, anchor)) {
                        if(curItem != null) drop(curItem);
                        opponent.transferItemAtAnchorTo(this, anchorOpp, anchor);
                    }
                }
            }
        }
    }

    /**
     * Hit the given enemy
     *
     * @param   enemy
     *          The opponent to hit
     *
     * @effect  This monster hits the given enemy
     *          | super.hit(enemy)
     */
    public void hit(Entity enemy)
            throws DeadEntityException, BrokenItemException, InvalidAnchorException, InvalidHolderException {
        super.hit(enemy);
    }

    /**
     * Initiates a fight with the given opponent
     * @param   opponent
     *          The opponent to fight
     * @effect  Initiates a fight with the given opponent
     *          | super.fight(opponent)
     *
     * @note    Makes the fight method of Entity public
     */
    @Override
    public void fight(Entity opponent) {
        super.fight(opponent);
    }

    /*
        Name
     */
    /**
     * Variable referencing the special character allowed in the name of a Monster
     */
    private static final String specialCharacters = "'";

    /**
     * Returns the special characters allowed in the name of a Monster
     */
    private static String getSpecialCharacters() {
        return specialCharacters;
    }
    /**
     * Checks if the given name is a valid name for a monster
     *
     * @param   name
     *          The name to check
     *
     * @return  True if and only if the given name begins with a capital letter and only contains the allowed characters
     *          (letters, apostrophes, spaces and the allowed special characters of a monster)
     *          | result == (name.matches("^[A-Z][a-zA-Z' specialCharacters]*"))
     */
    @Override
    public boolean canHaveAsName(String name) {
        return name.matches(String.format("^[A-Z][a-zA-Z' %s]*", getSpecialCharacters()));
    }
}
