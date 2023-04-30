package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class of Entities
 *
 * @author  Corteville Andrew
 *
 * @invar   Each entity must have a valid name
 *          | canHaveAsName(getName())
 * @invar   Each entity must have a valid maximum of hit points
 *          | canHaveAsMaxHitPoints(getMaxHitPoints())
 * @invar   Each entity must have a valid number of hit points when outside of combat
 *          | canHaveAsHitPoints(getHitPoints())
 * @invar   Each entity must have proper anchors
 *          | hasProperAnchors()
 */
public abstract class Entity implements ItemHolder {

    /*
        Constructors
     */

    /**
     * Initializes a new entity with the given name and maximum number of hit points alongside its actual hit points and
     * no anchors.
     *
     * @param   name
     *          The name of the new entity
     * @param   maxHitPoints
     *          The maximum hitPoints of the new entity
     *
     * @effect  Initializes this new entity with the given name and maximum number of hit points and its hit points set
     *          to the nearest lower prime of maxHitPoints and its anchors as an empty map
     *          | this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints))
     */
    @Raw
    protected Entity(String name, int maxHitPoints)
            throws IllegalArgumentException, InvalidAnchorException {
        this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints));
    }

    /**
     * Initializes this entity with the given name, maximum hit points, actual hit points and no anchors
     *
     * @param   name
     *          The name for the new entity
     * @param   maxHitPoints
     *          The maximum hit points of the new entity
     * @param   hitPoints
     *          The actual hit points of the new entity
     *
     * @pre     The given maximum hit points must be valid
     *          | canHaveAsMaxHitPoints(maxHitPoints)
     * @pre     The given hit points must be valid outside of combat
     *          | canHaveAsHitPoints(hitPoints)
     *
     * @effect  The name of this new entity is set to the given name
     *          | setName(name)
     * @effect  The maximum hit points of this new entity is set to the given name
     *          | setMaxHitPoints(maxHitPoints)
     * @effect  The hit points of this new entity is set to the given hit points
     *          | setHitPoints(hitPoints)
     * @post    The anchors of this entity is set to an empty map
     *          | new.getAnchors() == new HashMap<>()
     */
    @Raw
    protected Entity(String name, int maxHitPoints, int hitPoints)
            throws IllegalArgumentException, InvalidAnchorException {
        setName(name);
        setMaxHitPoints(maxHitPoints);
        setHitPoints(hitPoints);
    }
    /**
     * Initializes a new entity with the given name, maximumHitPoints and actual hit points and anchors.
     *
     * @param   name
     *          The name for the new entity
     * @param   maxHitPoints
     *          The maximum hit points of the new entity
     * @param   anchors
     *          A map of anchors for the new entity
     *
     * @effect  Initializes this new entity with the given name and maximum number of hit points and anchors
     *          and its hit points are set to the nearest lower prime of maxHitPoints or maxHitPoints if
     *          no such prime exists
     *          | this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints), anchors
     */
    @Raw
    protected Entity(String name, int maxHitPoints, HashMap<String, Item> anchors)
            throws IllegalArgumentException, InvalidAnchorException {
        this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints), anchors);
    }

    /**
     * Initializes a new Entity with the given name, maximum hit points, hit points and anchors
     *
     * @param   name
     *          The name for the new entity
     * @param   maxHitPoints
     *          The maximum hit points of the new entity
     * @param   hitPoints
     *          The hit points of the new entity
     * @param   anchors
     *          The anchors of the new entity
     *
     * @effect  Initializes this new Entity with the given name, maximum hit points and hit points. Its anchors are
     *          set to an empty map
     *          | this(name, maxHitPoints, hitPoints)
     * @post    The anchors of this entity is set to the given anchors
     *          | new.getAnchors() == anchors
     */
    protected Entity(String name, int maxHitPoints, int hitPoints, HashMap<String, Item> anchors)
        throws IllegalArgumentException, InvalidAnchorException {

        this(name, maxHitPoints, hitPoints);
        setAnchors(anchors);
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
     *
     * @post    Sets the isDead attribute of this entity to true.
     *          | new.isDead()
     * @post    Sets the hit points of this entity to 0
     *          | new.getHitPoints() == 0
     */
    public void die() {
        this.isDead = true;
        setHitPoints(0);
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
    private String name = null;

    /**
     * @return  The name of this entity
     */
    @Basic
    public String getName() {
        return name;
    }

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
    @Raw
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
     *  Variable referencing the maximum amount of hit points this entity can have
     */
    private int maxHitPoints = 100;

    /**
     * Variable referencing the current amount of hit points of the entity.
     */
    private int hitPoints = 97;

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

    /**
     * Sets the maximum hit points of this entity to the given maximum hit points
     *
     * @param   maxHitPoints
     *          The new maximum hit points of this entity
     *
     * @pre     The given maximum hit points must be valid
     *          | isValidMaxHitPoints(maxHitPoints)
     * @pre     The given maximum hit points must be greater than or equal to the current hit points
     *          | maxHitPoints >= getHitPoints()
     *
     * @post    The maximum hit points of this entity is set to the given maximum hit points
     *          | new.getMaxHitPoints() == maxHitPoints
     */
    @Raw
    @Model
    protected void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    /**
     * Increases the current maximum hit points with the given amount of hit points
     *
     * @param   hitPoints
     *          The amount of hit points that is to be added to the maximum hit points of this entity
     *
     * @pre     hitPoints should be a strictly positive integer
     *          | 0 < hitPoints
     * @pre     The hitPoints should be less than or equal to the maximum value of an Integer decreased with
     *          the current maximum hit points
     *          | hitPoints <= Integer.MAX_VALUE - getMaxHitPoints()
     *
     * @effect  The maximum hit points of this entity is increased with the given amount of hit points
     *          | setMaxHitPoints(getMaxHitPoints() + hitPoints)
     */
    public void increaseMaxHitPoints(int hitPoints) {
        setMaxHitPoints(getMaxHitPoints() + hitPoints);
    }

    /**
     * Decreases the maximum hit points of this entity with the given amount of hit points
     *
     * @param   hitPoints
     *          The amount of hit points that is to be removed from the maximum hit points of this entity
     *
     * @pre     hitPoints should be a strictly positive integer
     *          | hitPoints > 0
     * @pre     hitPoints should be less than or equal to the difference between the current and the maximum hit points
     *          of this entity
     *          | hitPoints <= (getMaxHitPoints() - getHitPoints())
     *
     * @effect  The maximum hit points of this entity is decreased with the given amount of hit points.
     *          | setMaxHitPoints(getMaxHitPoints() - hitPoints)
     */
    public void decreaseMaxHitPoints(int hitPoints) {
        setMaxHitPoints(getMaxHitPoints() - hitPoints);
    }


    /**
     * Sets the hit points of this entity to the given number of hit points
     *
     * @param   hitPoints
     *          The new number of hit points for this entity
     *
     * @pre     The given hit points must be valid (if the entity is not inside of combat)
     *          | canHaveAsHitPoints(hitPoints)
     *
     * @post    The hit points of this entity is set to the given amount of hit points
     *          | new.getHitPoints() == hitPoints
     */
    @Model
    @Raw
    protected void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * This entity takes damage equal to the given amount
     *
     * @param   amount
     *          The amount of damage that is taken
     *
     * @pre     The given amount must be a strictly positive integer
     *          | amount > 0
     *
     * @effect  If the given amount of damage taken is greater than the current hit points of this entity
     *          then this entity dies.
     *          | if(amount > getHitPoints)
     *          | then die()
     * @effect  If the given amount of damage taken does not exceed the current hit points then the hit points of this
     *          entity then the hit points of this entity is decreased with the given amount
     *          | if( !(amount > getHitPoints()) )
     *          | then setHitPoints(getHitPoints() - amount)
     */
    public void takeDamage(int amount) {
        if(amount > getHitPoints()) {
            die();
        } else {
            setHitPoints(getHitPoints() - amount);
        }
    }

    /**
     *  Returns the first prime that is smaller than or equal to the given number or number if no prime exists that
     *  is smaller or equal to number
     *
     * @param   number
     *          The given number
     *
     * @return  The first prime number that is smaller than the given number
     *          | ( isPrime(result) || result == number ) &&
     *          | ( for num in (result+1)..number:
     *          |       !isPrime(num)
     *          | )
     */
    protected static int getFirstLowerPrime(int number) {
        while(!isPrime(number) && number > 1) number--;
        return number;
    }

    /**
     * Checks if the given number maximum hit points are valid.
     *
     * @param   maxHitPoints
     *          The number of maximum hit points to check
     *
     * @return  True if and only if the given maximum hit points strictly positive and greater than or equal to the
     *          actual hit points of this entity
     *          | result ==
     *          |   ( 0 < maxHitPoints ) &&
     *          |   ( getHitPoints() <= maxHitPoints)
     *
     * @note    Not sure if needed to check if maxHitPoints >= getHitPoints
     */
    public boolean canHaveAsMaxHitPoints(int maxHitPoints) {
        return 0 < maxHitPoints && getHitPoints() <= maxHitPoints;
    }

    /**
     * Checks if the given number of hits points are valid for this entity whether it is in combat or not
     *
     * @param   hitPoints
     *          The number of hit points to check
     * @return  True if and only if the given hit points are positive and less than or equal to the maximum hitPoints
     *          of this entity and the given hit points is a prime number
     *          | result ==
     *          |   ( 0 <= hitPoints ) &&
     *          |   ( hitPoints <= getMaxHitPoints() )
     */
    public boolean canHaveAsHitPoints(int hitPoints) {
        return (0 <= hitPoints ) && (hitPoints <= getMaxHitPoints()) && (isPrime(hitPoints));
    }



    /**
     * Checks if the given number is prime.
     *
     * @param   number
     *          The number to check
     * @return  True if and only if the given number is prime.
     *          | result ==
     *          |   for num in 2..Math.sqrt(num):
     *          |       number % num == 0
     */
    private static boolean isPrime(int number) {
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
     * @invar   anchors references an effective map.
     *          | anchors != null
     * @invar   Each anchor should be effective.
     *          | for each anchor, item in anchors:
     *          |   anchors != null
     * @invar   Each mapping references either an item that can be held at its associated anchor.
     *          | for each anchor, item in anchors:
     *          |   canHaveItemAtAnchor(item, anchor)
     * @invar   Each mapping references either an item that references back to this entity or is not effective.
     *          | for each anchor, item in anchors:
     *          |   item == null || item.getHolder() == this
     * @invar   Each anchor should be able to hold its associated item.
     *          | for each anchor, item in anchors:
     *          |   canHaveItemAt(item, anchor)
     */
    private Map<String, Item> anchors = new HashMap<>();

    /**
     *  Sets the anchors of this entity to the given Map of anchors
     *
     * @param   anchors
     *          The new anchors
     *
     */
    @Model
    @Raw
    private void setAnchors(HashMap<String, Item> anchors) throws InvalidAnchorException {
        this.anchors = anchors;
    }

    /**
     * Sets the given anchor to the given item
     *
     * @param   anchor
     *          The name of the anchor where to add the item to
     * @param   item
     *          The item to add to the anchor
     *
     * @post    The item is added to the given anchor
     *          | new.getItemAt(anchor) == item
     * @throws  IllegalArgumentException
     *          If the entity does not have the given anchor
     *          | !hasAnchor(anchor)
     * @throws  InvalidAnchorException
     *          If the given anchor already has an item associated with it.
     *          | getItemAt(anchor) != null
     * @throws  InvalidAnchorException
     *          The item cannot be located at the given anchor
     *          | !canHaveItemAt(item, anchor)
     */
    @Model
    private void setAnchor(String anchor, Item item) throws InvalidAnchorException, IllegalArgumentException {
        if(!hasAnchor(anchor)) throw new IllegalArgumentException("This anchor does not exist");
        if(!canHaveItemAtAnchor(item, anchor)) throw new InvalidAnchorException(this, item, anchor);
        if(getItemAt(anchor) != null) throw new InvalidAnchorException(this, item, anchor);

        try {
            anchors.replace(anchor, item);
        } catch (Exception e) {
            // Should not happen
            assert false;
        }

    }

    /**
     * Checks if the given anchor exists on this entity
     *
     * @param   anchor
     *          The anchor to check
     * @return  True if and only if the given anchor is an effective anchor of this entity , otherwise return false
     *          | result ==
     *          |   ( anchor != null &&
     *          |   anchors.containsKey(anchor) )
     */
    public boolean hasAnchor(String anchor) {
        return anchor != null && anchors.containsKey(anchor);
    }

    /**
     * Returns a set of the names of anchor associated with this entity
     */
    @Basic
    public Set<String> getAnchorPoints() {
        return anchors.keySet();
    }

    /**
     * Returns the Item associated with the given anchor name
     *
     * @param   anchorPoint
     *          The name of the desired anchor point
     * @return  The item associated with the given anchor name or null if no item is associated with the given anchor
     *          | result == anchors.get(anchorPoint)
     * @throws  IllegalArgumentException
     *          This entity has no anchor with the given anchor name
     *          | !anchor.contains
     */
    @Basic
    public Item getItemAt(String anchorPoint) throws IllegalArgumentException {
        if(hasAnchor(anchorPoint))
        {
                return anchors.get(anchorPoint);
        }
        throw new IllegalArgumentException(anchorPoint + " does not exist on " + this);
    }

    /**
     * Checks if the given item can be held at the given anchor
     *
     * @param   item
     *          The given item
     * @param   anchor
     *          The given anchor
     *
     * @return  True if and only if the given item can be held at the given anchor of this entity.
     *          | result == (
     *          |   (getItemAt(anchor) == null || getItemAt(anchor) == item) &&
     *          |       (item == null || !isDead() && (
     *          |           (item instance of Purse && anchor.equals("Belt")) ||
     *          |           ( !(item.getClass() instance of Purse.class) && !anchor.equals("Belt") && !item.isBroken()))))
     * @throws  IllegalArgumentException
     *          The given anchor is not an anchor of this entity
     *          | !hasAnchor(anchor)
     */
    public boolean canHaveItemAtAnchor(Item item, String anchor) throws IllegalArgumentException {
        if(!hasAnchor(anchor)) throw new IllegalArgumentException("This entity does not have an anchor named: " + anchor);

        return (item == null || !isDead() && (
                        (item.getClass() == Purse.class && anchor.equals("Belt")) ||
                                (item.getClass() != Purse.class && !anchor.equals("Belt") && !item.isBroken()))) &&
                (getItemAt(anchor) == null || getItemAt(anchor) == item);
    }

    /**
     * Checks if the anchors of this backpack are valid
     *
     * @return  True if and only if each item can be held at its anchor and the items holder is this entity, otherwise
     *          return false
     *          | result == (
     *          |   for each anchor, item in anchors:
     *          |       (canHaveItemAtAnchor(item, anchor) && item.getHolder() == this)
     */
    public boolean hasProperAnchors() {
        for (Map.Entry<String, Item> entry: anchors.entrySet()) {
            String anchor = entry.getKey();
            Item item = entry.getValue();
            if(!canHaveItemAtAnchor(item, anchor) || item.getHolder() != this) return false;
        }
        return true;
    }

    /**
     * Returns the weight of all the items that this entity holds
     *
     * @return  The weight of all items that this entity holds
     *          | result == (
     *          |   weight +
     *          |   for each item in anchors.values():
     *          |       item.getWeight()
     */
    public double getLoad() {
        double weight = 0;
        for(Item item: anchors.values()) {
            if(item != null) weight += item.getWeight();
        }
        return weight;
    }

    //TODO documentation
    /**
     * Returns how many items of a given type this entity holds directly of indirectly
     * @param   type
     *          The given type
     * @return  The amount of items directly and indirectly held by this entity.
     *          | result == (
     *          |
     *          |
     */
    protected int getNbOfItemsOfTypeHeld(Class<? extends Item> type) {
        int amount = 0;
        for (Item item: anchors.values()) {
            if (item.getClass() == type) amount++;
            if (item.getClass() == Backpack.class) amount += ((Backpack) item).getNbOfItemsOfTypeHeld(type);
        }
        return amount;
    }

    /**
     * Returns the maximum weight this entity can hold
     * @return  The maximum weight this entity can hold
     *          | ? 0
     */
    public abstract double getCapacity();

    /*
        Item Holder
     */

    /**
     * Checks if this entity can pick up the given item
     *
     * @param   item
     *          The item to check
     *
     * @return  True if and only if picking up the item doesn't exceed the maximum number of armors this entity can hold and
     *          the given item is not broken or a purse and either is already holden by
     *          this entity or this entity is able to pick up the weight of the given item.
     *          | result == (
     *          |   !(item instance of Armor && getNbItemsOfTypeHeld(Armor.class) >= 2) &&
     *          |    (item instance of Purse || !item.isBroken()) &&
     *          |    (holdsItem(item) || canPick(item.getWeight())
     *          |   )
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     */
    @Override
    public boolean canPickup(Item item) throws IllegalArgumentException {
        if (item == null) throw new IllegalArgumentException("The given item is not effective");

        return !(item.getClass() == Armor.class && getNbOfItemsOfTypeHeld(Armor.class) >= 2) &&
                (item.getClass() == Purse.class || !item.isBroken()) && (
                    holdsItem(item) || canPickup(item.getWeight())
                );
    }

    /**
     * Checks if this entity can pick up the given weight
     *
     * @param   weight
     *          The weight to check
     *
     * @return  True if and only if the given weight will not exceed the capacity of this entity if added
     *          | result ==
     *          |   ( getLoad() + weight <= getCapacity() )
     * @throws  IllegalArgumentException
     *          The given weight is negative
     *          | weight < 0
     */
    @Override
    public boolean canPickup(double weight) throws IllegalArgumentException {
        if(weight < 0) throw new IllegalArgumentException("The given weight cannot be less than 0");
        return getLoad() + weight <= getCapacity();
    }

    /**
     * Checks if this entity holds the given item
     * @param   item
     *          The given item
     *
     * @return  True if and only if the given item is holden by this entity
     *
     * @throws IllegalArgumentException
     */
    @Override
    public boolean holdsItem(Item item) throws IllegalArgumentException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        return anchors.containsValue(item);
    }

    @Override
    public void drop(Item item) throws IllegalArgumentException, InvalidHolderException {

    }

    /**
     * Adds an item from the ground to free anchor of this entity
     *
     * @param   item
     *          The item to add
     *
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     * @throws  IllegalArgumentException
     *          The given item does not lie on the ground
     *          | item.getHolder() != null
     *
     */
    @Override
    @Raw
    public void pickup(Item item)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException, InvalidAnchorException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!item.liesOnGround()) throw new IllegalArgumentException("The given item doesn't lie on the ground");

        for (String anchor: getAnchorPoints()) {
            try {
                if(canHaveItemAtAnchor(item, anchor)) {
                    pickup(item, anchor);
                    return;
                }
            } catch (Exception e) {
                // Should not happen
                assert false;
            }

        }


        item.setHolder(this);
    }

    /**
     * Adds an item to the given anchor
     *
     * @param   item
     *          The item to add
     * @param   anchor
     *          The given to which to add the item
     *
     *
     *
     */
    public void pickup(Item item, String anchor) throws IllegalArgumentException, InvalidAnchorException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!hasAnchor(anchor)) throw new IllegalArgumentException(this + "has no anchor named: " + anchor);
        if(!canHaveItemAtAnchor(item, anchor)) throw new InvalidAnchorException(this, item, anchor);
        try {
            setAnchor(anchor, item);
        } catch (Exception e) {

            assert false;
        }
    }
}
