package rpg;

import be.kuleuven.cs.som.annotate.Model;

import java.util.HashMap;

/**
 * A class of Entities
 *
 * @invar   Each entity must have a valid name
 *          | canHaveAsName(getName())
 * @invar   Each entity must have a valid maximum of hit points
 *          | isValidMaxHitPoints(getMaxHitPoints())
 * @invar   Each entity must have a valid number of hit points
 *          | canHaveAsHitPoints(getHitPoints())
 * @invar   Each entity must have proper anchors
 *          | hasProperAnchors()
 */
public abstract class Entity implements ItemHolder {

    /*
        Constructors
     */

    /**
     * @param   name
     *          The name of the new entity
     * @param   maxHitPoints
     *          The maximum hitPoints of the new entity
     *
     * @pre     maxHitPoints should be valid
     *          | isValidMaxHitPoints(maxHitPoints())
     *
     * @effect
     * @effect
     */
    public Entity(String name, int maxHitPoints) throws IllegalArgumentException {
        setName(name);
        setMaxHitPoints(maxHitPoints);
        setHitPointsToLowerPrime();
    }

    /*
        Destructor
     */

    /**
     * Variable referencing the state of this entity. i.e. true means this entity is dead and thus not effective.
     */
    private boolean isDead = false;

    /**
     * Kills this entity
     */
    public void die() {

        this.isDead = true;
    }

    /**
     * @return  whether this entity is dead.
     */
    public boolean isDead() {
        return isDead;
    }

    /*
        Name (DEFENSIVE)
     */
    /**
     * Variable referencing the name of the entity.
     */
    private String name;

    /**
     * If the given name is valid, sets to name to the given name.
     *
     * @param   name
     *          The given name
     * @post    If the given name is valid, the name of this entity is set to the given name
     *          | if (isValidName(name))
     *          | then new.getName() == name
     * @throws  IllegalArgumentException
     *          If the given name is not valid.
     *          | !isValidName(name)
     */
    public void setName(String name) throws IllegalArgumentException {
        if(canHaveAsName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(name + " is not a valid name for a " + getClass().toString());
        }
    }


    /**
     * Checks if the given name is a valid entity name
     *
     * @param   name
     *          The name to check
     *
     * @return  True if and only if the first character of the given name is a capitalized letter
     */
    public boolean canHaveAsName(String name) {
        return name.matches("^[A-Z].*");
    }

    /*
        HitPoints (Nominal)
     */

    /**
     *  Variable referencing the maximum amount of hitpoints this entity can have
     */
    private int maxHitPoints = 2;

    /**
     * Variable referencing the current amount of hitpoints of the entity.
     */
    private int hitPoints = 1;

    /**
     * @return  The maximum HP this entity can have
     */
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    /**
     * @return  The current HP of this entity
     */
    public int getHitPoints() {
        return hitPoints;
    }

    protected void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    protected void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     *
     */
    protected void setHitPointsToLowerPrime() {
        int hitPoints = getHitPoints();
        while(!isPrime(hitPoints) && hitPoints > 1) hitPoints--;
        setHitPoints(hitPoints);
    }

    /**
     * Checks if the given maximum hit points are valid.
     */
    public static boolean isValidMaxHitPoints(int maxHitPoints) {
        return maxHitPoints > 0;
    }

    /**
     * Checks if the given
     *
     * @param   hitPoints
     * @return
     */
    public boolean canHaveAsHitPoints(int hitPoints) {
        return 0 <= hitPoints && hitPoints <= getMaxHitPoints();
    }



    /**
     * Checks if the given number is prime.
     *
     * @param   number
     *          The number to check
     * @return  True if and only if the given number is prime.
     *          |
     */
    private static boolean isPrime(long number) {
        if (number <= 1) return false;
        for (int i=2; i <= Math.sqrt(number); i++) if (number % i == 0) return false;
        return true;
    }

    /*
        Anchors
     */

    /**
     * Map of all anchors of this entity of its name and which item it holds.
     *
     * @invar   anchors references an effective map
     *          | anchors != null
     * @invar   Each mapping references either a non-broken item or is not effective
     *          | for each key, item in anchors:
     *          |   item == null || !item.isBroken()
     * @invar   Each mapping references either an item that references back to this entity or is not effective
     *          | for each key, item in anchors:
     *          |   item == null || item.getHolder() == this
     * @invar   Each key of this map should be a valid anchorName
     */
    private HashMap<String, Item> anchors = new HashMap<>();

    /**
     *
     */
    @Model
    private void setAnchors(HashMap<String, Item> anchors) {
        this.anchors = anchors;
    }

    public Item getAnchor(String anchorName) throws IllegalArgumentException {
        if(anchors.containsKey(anchorName))
        {
                if (anchors.get(anchorName) != null) {
                    return anchors.get(anchorName);
                }
                return null;
        }
        throw new IllegalArgumentException(anchorName + " does not exist on " + this);
    }

}
