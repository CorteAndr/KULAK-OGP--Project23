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
     * @effect  Initializes this monster with the given name, maximum hit points, actual hit points, anchors
     *          and protection
     *          | super(name, maxHitPoints, hitPoints, anchors, protection)
     * @effect  The anchors of this monster are set to the given anchors
     *          | setAnchors(anchors)
     * @post    The capacity of this new monster is set to the given capacity.
     *          | new.getCapacity() == capacity
     */
    @Raw
    public Monster(String name, int maxHitPoints, int hitPoints, Collection<Anchorpoint> anchors, int protection, int damage)
            throws IllegalArgumentException, InvalidHolderException {
        super(name, maxHitPoints, hitPoints, anchors, protection);
        setDamage(damage);
        this.capacity = (new Random()).nextInt(10, 151);
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
     * @effect  Initializes this new monster with the given name, maximum hit points and protection and a random
     *          amount of anchors.
     *          | super(name, maxHitPoints, getRandomAnchors(), protection)
     * @effect  The damage of this new monster is set to the given damage
     *          | setDamage(damage)
     * @post    The capacity of this new Monster is set to a random integer between 10 and 150
     *          | new.getCapacity() >= 10 && new.getCapacity() <= 150
     */
    @Raw
    public Monster(String name, int maxHitPoints, int protection, int damage)
            throws BrokenItemException {
        super(name, maxHitPoints, getRandomAnchors(), protection);
        setDamage(damage);
        pickupItems(getRandomItemsFor(getAnchorPoints()));
        this.capacity = getLoad();
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
     * @effect  Initializes this new Monster with the given name, maximum hit points and protection alongside the
     *          given items and anchors that can hold all those items.
     *          | super(name, maxHitPoints, protection)
     * @effect  The damage of this new Monster is set to the given damage
     *          | setDamage(damage)
     * @post    The capacity of this new Monster is set load of the previously created anchors
     *          | new.getCapacity() == getLoad()
     */
    @Raw
    public Monster(String name, int maxHitPoints, int protection, int damage, Collection<Item> items)
            throws IllegalArgumentException, InvalidHolderException {
        super(name, maxHitPoints, Entity.getFirstLowerPrime(maxHitPoints), getAnchorsFor(items), items, protection);
        setDamage(damage);
        this.capacity = getLoad();
    }

    /**
     * Generates a Collection of anchors that can hold all the given items
     *
     * @param   items
     *          The items to find anchor-points for
     *
     * @return  A collection of anchor-points such that each item can be allocated
     *          |
     * @throws  IllegalArgumentException
     *          The given items are not effective
     *          | items == null
     * @throws  IllegalArgumentException
     *          There is no anchor that can hold a certain that hasn't already been used
     *          | ?
     */
    //TODO formal return/throw
    private static Collection<Anchorpoint> getAnchorsFor(Collection<Item> items) throws IllegalArgumentException {
        if(items == null) throw new IllegalArgumentException("The given items are not effective");
        Collection<Anchorpoint> result = new HashSet<>();
        for(Item item: items) {
            boolean foundAnchor = false;
            for (Anchorpoint anchor: Anchorpoint.values()) {
                if(!result.contains(anchor) && anchor.canHoldItem(item)) {
                    foundAnchor = true;
                    result.add(anchor);
                }
            }
            if(!foundAnchor) throw new IllegalArgumentException("No new anchor found for " + item);
        }
        return result;
    }

    /**
     * Generates a random map of anchors with random items
     *
     * @return  A collection that contains a random amount of anchors
     *          | for each anchor in result:
     *          |   anchor instanceof Anchorpoint
     */
    private static Collection<Anchorpoint> getRandomAnchors() {
        Collection<Anchorpoint> result = new HashSet<>();
        for(Anchorpoint anchor: Anchorpoint.values()) {
            if ((new Random()).nextBoolean()) {
                result.add(anchor);
            }
        }
        return result;
    }

    private static Collection<Item> getRandomItemsFor(Collection<Anchorpoint> anchors) {
        if(anchors == null) throw new IllegalArgumentException("The given anchors are not effective");
        Collection<Item> result = new ArrayList<>();
        for (Anchorpoint anchor: anchors) {
            if (anchor == Anchorpoint.BELT) {
                result.add(new Purse(0.1, 100));
            } else {
                switch ((new Random()).nextInt(0, 3)) {
                    case 0 -> result.add(new Weapon(5.10, 14));
                    case 1 -> result.add(new Armor(-1, 13.2, 50, 25));
                    case 2 -> result.add(new Backpack(4.3, 20, 60));
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
            for (Anchorpoint anchor: getAnchorPoints()) {
                Item curItem = getItemAt(anchor);
                if(curItem != null) {
                    double dWeight = item.getWeight() - curItem.getWeight();
                    if(anchor.canHoldItem(item) && (dWeight <= 0 || canPickup(dWeight))) {
                        drop(curItem);
                        pickup(item, anchor);
                        break;
                    }
                } else if (canHaveItemAtAnchor(item, anchor)) {
                    pickup(item, anchor);
                    break;
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
