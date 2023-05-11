package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.DeadEntityException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.*;

/**
 * An abstract class of Entities
 *
 * @author  Corteville Andrew
 *
 * @invar   Each entity must have a valid name
 *          | canHaveAsName(getName())
 * @invar   Each entity must have a valid maximum of hit points
 *          | canHaveAsMaxHitPoints(getMaxHitPoints())
 * @invar   Each entity must have a valid number of hit points
 *          | canHaveAsHitPoints(getHitPoints())
 * @invar   Each entity must have proper anchors
 *          | hasProperAnchors()
 * @invar   Each entity must have a valid base protection
 *          | canHaveAsBaseProtection(getBaseProtection())
 * @invar   Each entity must have a valid capacity
 *          | canHaveAsCapacity(getCapacity())
 */
public abstract class Entity implements ItemHolder {

    /*
        Constructors
     */

    /**
     * Initializes a new entity with the given name and maximum number of hit points alongside its intrinsic protection
     * and empty anchors.
     *
     * @param   name
     *          The name of the new entity
     * @param   maxHitPoints
     *          The maximum hitPoints of the new entity
     * @param   protection
     *          The intrinsic protection of the new entity
     *
     * @effect  Initializes this new entity with the given name and maximum number of hit points and its hit points set
     *          to the nearest lower prime of maxHitPoints and its anchors as an empty map
     *          | this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints), protection)
     */
    @Raw
    protected Entity(String name, int maxHitPoints, int protection)
            throws IllegalArgumentException {
        this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints), protection);
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
     * @param   protection
     *          The intrinsic protection of the new entity
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
    protected Entity(String name, int maxHitPoints, int hitPoints, int protection)
            throws IllegalArgumentException {
        setName(name);
        setMaxHitPoints(maxHitPoints);
        setHitPoints(hitPoints);
        if(!canHaveAsBaseProtection(protection)) protection = getDefaultProtection();
        this.baseProtection = protection;
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
    protected Entity(String name, int maxHitPoints, Collection<Anchorpoint> anchors, int protection)
            throws IllegalArgumentException {
        this(name, maxHitPoints, getFirstLowerPrime(maxHitPoints), anchors, protection);
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
     * @effect  The anchors of this entity are set to the given anchors
     *          | setAnchors(anchors)
     *
     */
    protected Entity(String name, int maxHitPoints, int hitPoints, Collection<Anchorpoint> anchors, int protection)
        throws IllegalArgumentException {
        this(name, maxHitPoints, hitPoints, protection);
        setAnchors(anchors);
    }

    /**
     * Initializes an Entity with the given name, maximum hit points, actual hit points, anchor points, items and
     * protection
     *
     * @param   name
     *          The name for the new Entity
     * @param   maxHitPoints
     *          The maximum hit points of the new Entity
     * @param   hitPoints
     *          The actual hit points of the new Entity
     * @param   anchors
     *          The anchor points of the new Entity
     * @param   items
     *          The items of the new Entity
     * @param   protection
     *          The intrinsic protection of the new Entity
     *
     * @effect  Initializes this Entity with the given name, maximum hit points, actual hit points, anchors and protection
     *          | super(name, maxHitPoints, hitPoints, anchors, protection)
     * @effect  Allocates all given items to anchors of this new Entity
     *          | pickupItems(items)
     */
    protected Entity(String name, int maxHitPoints, int hitPoints, Collection<Anchorpoint> anchors, Collection<Item> items, int protection)
            throws IllegalArgumentException {
        this(name, maxHitPoints, hitPoints, anchors, protection);
        pickupItems(items);
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
     * Variable referencing whether this entity is fighting or not
     */
    private boolean isFighting = false;

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
     * @return  Whether this entity is fighting or not
     */
    public boolean isFighting() {
        return isFighting;
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
     *          entity then the hit points of this entity is decreased with the given amount and lowered to the first
     *          prime
     *          | if( !(amount > getHitPoints()) )
     *          | then setHitPoints(getFirstLowerPrime(getHitPoints() - amount))
     */
    public void takeDamage(int amount) {
        if(amount > getHitPoints()) {
            die();
        } else {
            setHitPoints(getFirstLowerPrime(getHitPoints() - amount));
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
    public static int getFirstLowerPrime(int number) {
        while(!isPrime(number) && number > 1) number--;
        return number;
    }

    /**
     * Set this entity as fighting
     *
     * @post    Sets the fighting attribute to true
     *          | new.isFighting()
     */
    protected void startFighting() {
        this.isFighting = true;
    }

    /**
     * Sets this entity as not fighting
     *
     * @post    Sets the fighting attribute to false
     *          | !new.isFighting()
     */
    protected void stopFighting() {
        this.isFighting = false;
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
     * Checks if the given number of hits points are valid for this entity when its outside combat
     *
     * @param   hitPoints
     *          The number of hit points to check
     * @return  True if and only if the given hit points are positive and less than or equal to the maximum hitPoints
     *          of this entity and this entity is either fighting or the given hit points is a prime number
     *          | result == (
     *          |   ( 0 <= hitPoints ) &&
     *          |   ( hitPoints <= getMaxHitPoints()) &&
     *          |   (isFighting || isPrime(hitPoints))
     *          | )
     */
    public boolean canHaveAsHitPoints(int hitPoints) {
        return (0 <= hitPoints ) && (hitPoints <= getMaxHitPoints()) && (isFighting || (isPrime(hitPoints)));
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
        Anchorpoint
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
     */
    private Map<Anchorpoint, Item> anchors = new HashMap<>();


    /**
     * Adds the given anchor points to this entity
     *
     * @param   anchors
     *          The anchor points to add
     *
     * @post    Each anchorpoint in the given anchor points is added if this did not have the anchor already
     *          | for each anchor in anchorpoints:
     *          |   new.hasAnchor(anchor)
     */
    protected void setAnchors(Collection<Anchorpoint> anchors) {
        for (Anchorpoint anchor: anchors) {
            if(!hasAnchor(anchor)) this.anchors.put(anchor, null);
        }
    }

    /**
     * Allocates all the given item at anchors
     *
     * @param   items
     *          The items to allocate
     *
     * @pre     Each item should be able to be picked up by this entity
     *          | for each item in items:
     *          |   canPickup(item)
     *
     * @effect  Each item is allocated to an anchor of this Entity
     *          | for each item in items:
     *          |   holdsItemDirectly(item)
     *
     * @throws  IllegalArgumentException
     *          When no anchor is found for a specific item
     */
    //TODO documentation
    @Model
    @Raw
    protected void pickupItems(Collection<Item> items)
            throws IllegalArgumentException {
        if(items != null) {
            if(items.stream().anyMatch(Armor.class::isInstance) && hasAnchor(Anchorpoint.BODY)) {
                anchors.put(Anchorpoint.BODY, items.stream().filter(Armor.class::isInstance).findFirst().get());
            }
            for(Item item: items) {
                boolean foundAnchor = false;
                if(item instanceof Armor) {
                    if(hasAnchor(Anchorpoint.BODY) && canHaveItemAtAnchor(item, Anchorpoint.BODY))
                        try {
                            foundAnchor = true;
                            item.setHolder(this);
                            anchors.put(Anchorpoint.BODY, item);
                        } catch (Exception e) {
                            assert false;
                        }

                } else {
                    for (Anchorpoint anchor : getAnchorPoints()) {
                        if (canHaveItemAtAnchor(item, anchor) && anchor.canHoldItem(item)) {

                            foundAnchor = true;
                            item.setHolder(this);
                            anchors.put(anchor, item);
                            break;
                        }
                    }
                    if (!foundAnchor) throw new IllegalArgumentException(
                            String.format("No anchor was found for %h at %h", item, this));
                }
            }
        }
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
     * @effect  If the given item is held by another item holder than said item holder drops the given item
     *          | if (item.getHolder() != null || item.getHolder() != this)
     *          | then item.getHolder().drop(item)
     * @effect  The holder of the given item is set to this entity
     *          | item.setHolder(this)
     * @throws  IllegalArgumentException
     *          If the entity does not have the given anchor
     *          | !hasAnchor(anchor)
     * @throws  InvalidAnchorException
     *          If the given anchor already has an item associated with it.
     *          | getItemAt(anchor) != null
     * @throws  InvalidAnchorException
     *          The item cannot be located at the given anchor
     *          | !canHaveItemAt(item, anchor)
     * @throws  InvalidHolderException
     *          The given item cannot have this entity as its holder.
     */
    @Model
    private void setAnchor(Anchorpoint anchor, Item item)
            throws InvalidAnchorException, IllegalArgumentException, InvalidHolderException {
        if(!hasAnchor(anchor)) throw new IllegalArgumentException("This anchor does not exist");
        if(!canHaveItemAtAnchor(item, anchor)) throw new InvalidAnchorException(this, item, anchor);
        if(getItemAt(anchor) != null) throw new InvalidAnchorException(this, item, anchor);
        try {
            if(item.getHolder() != null && item.getHolder() != this) item.getHolder().drop(item);
            item.setHolder(this);
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
    public boolean hasAnchor(Anchorpoint anchor) {
        return anchor != null && getAnchorPoints().contains(anchor);
    }

    /**
     * Returns a set of the names of anchor associated with this entity
     */
    @Basic
    public Set<Anchorpoint> getAnchorPoints() {
        return anchors.keySet();
    }

    /**
     * Returns all items held by this entity
     */
    protected Collection<Item> getItems() {
        return anchors.values();
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
    public Item getItemAt(Anchorpoint anchorPoint) throws IllegalArgumentException {
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
     * @return  True if and only if the given item can be held at the given anchor of this entity, otherwise return false.
     *          | result == (
     *          |   item == null ||
     *          |   !isDead() && (
     *          |       anchor.canHoldItem(item) && (
     *          |           getItemAt(anchor) == null ||
     *          |           getItemAt(anchor) == item
     *          |       )
     *          |   )
     *          |)
     * @throws  IllegalArgumentException
     *          The given anchor is not an anchor of this entity
     *          | !hasAnchor(anchor)
     */
    public boolean canHaveItemAtAnchor(Item item, Anchorpoint anchor) throws IllegalArgumentException {
        if(!hasAnchor(anchor)) throw new IllegalArgumentException("This entity does not have an anchor named: " + anchor);

        return (item == null || !isDead() && (anchor.canHoldItem(item) &&
                (getItemAt(anchor) == null || getItemAt(anchor) == item)));
    }


    /**
     * Return the anchor of the given item
     * @param   item
     *          The item to search the anchor of
     *
     * @return  The anchor of the given item
     *          | getItemAt(result) == item
     *
     * @throws  IllegalArgumentException
     *          The given item is not directly held by this entity
     *          | !holdsItemDirectly(item)
     */
    public Anchorpoint getAnchorOf(Item item) throws IllegalArgumentException {
        if(!holdsItemDirectly(item)) throw new IllegalArgumentException("The item is not directly held by this entity");
        for(Anchorpoint anchor: getAnchorPoints()) {
            if(getItemAt(anchor) == item) return anchor;
        }
        // Should not happen
        assert false;

        return null;
    }

    /**
     * Checks if the anchors of this backpack are valid
     *
     * @return  True if and only the invariants of anchors are met
     *          | result == (
     *          |   for each anchor, item in anchors:
     *          |       (canHaveItemAtAnchor(item, anchor) && (item == null || item.getHolder() == this)
     *          | )
     */
    public boolean hasProperAnchors() {
        for (Map.Entry<Anchorpoint, Item> entry: anchors.entrySet()) {
            Anchorpoint anchor = entry.getKey();
            Item item = entry.getValue();
            if(!canHaveItemAtAnchor(item, anchor) || (item != null && item.getHolder() != this)) return false;
        }
        return getLoad() <= getCapacity();
    }

    /**
     * Returns the weight of all the items that this entity holds
     *
     * @return  The weight of all effective items that this entity holds
     *          | result == (
     *          |   0 +
     *          |   for each item in this.getItems():
     *          |       if(item != null)
     *          |       then item.getWeight()
     */
    //TODO ?
    public double getLoad() {
        double weight = 0.00;
        for(Item item: getItems()) {
            if(item != null) weight += item.getWeight();
        }
        return weight;
    }

    /**
     * Returns the total value of items held
     *
     * @return  The value of all effective items that this entity holds
     *          | result == (
     *          |   0 +
     *          |   for each item in this.getItems():
     *          |       if(item != null)
     *          |       then item.getValue()
     */
    public int getValueHeld() {
        int value = 0;
        for(Item item: getItems()) {
            if(item != null) value += item.getValue();
        }
        return value;
    }

    //TODO formal correct?
    /**
     * Returns how many items of a given type this entity holds directly of indirectly
     * @param   type
     *          The given type
     * @return  The amount of items directly and indirectly held by this entity.
     *          | result == (
     *          |   0 +
     *          |   for each item in anchor.values():
     *          |   if(item instance of type)
     *          |   then +1
     *          |   if(item instance of Backpack)
     *          |   then +((Backpack) item).getNbOfItemsOfTypeHeld(type)
     *          | )
     */
    @Override
    public int getNbOfItemsOfTypeHeld(Class<? extends Item> type) {
        int amount = 0;
        for (Item item: anchors.values()) {
            if (item == null) continue;
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


    /**
     * Checks if the given capacity is a valid capacity for this entity
     *
     * @param   capacity
     *          The capacity to check
     *
     * @return  True if and only if the given capacity is greater than or equal to the total weight of the items this
     *          entity holds
     *          | result == (capacity >= getLoad())
     */
    public boolean canHaveAsCapacity(double capacity) {
        return capacity >= getLoad();
    }
    /*
        Item Holder
     */

    /**
     * Checks if this entity can pick up the given item, without considering anchors
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

        return (item instanceof Purse || !item.isBroken()) && (
                    holdsItemDirectly(item) || canPickup(item.getWeight())
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
     * Checks if this entity directly holds the given item
     * @param   item
     *          The given item
     *
     * @return  True if and only if the given item is holden by this entity
     *          | anchors.containsValue(item)
     *
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     */
    @Override
    public boolean holdsItemDirectly(Item item) throws IllegalArgumentException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        return anchors.containsValue(item);
    }

    /**
     * Checks if the given item is held by this entity (directly of indirectly)
     *
     * @param   item
     *          The item to search
     * @return  True if and only if the given item is directly or indirectly held by this entity, otherwise return false.
     *          | this == item.getHighestHolder();
     */
    @Override
    public boolean holdsItem(Item item) {
        return item.getHighestHolder() == this;
    }

    /**
     * Drops this on the ground
     *
     * @param   item
     *          The given item on the ground
     *
     * @post    The item is no longer held directly by this entity
     *          | !new.holdsItemDirectly(item)
     * @effect  
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     * @throws  IllegalArgumentException
     *          The given item is not directly held by this entity
     *          | !holdsItemDirectly(item)
     */
    @Override
    @Raw
    public void drop(Item item) throws IllegalArgumentException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!holdsItemDirectly(item)) throw new IllegalArgumentException("The given item is not held by this entity");

        anchors.put(getAnchorOf(item), null);
        item.setHolder(null);
    }

    /**
     * Adds an item from the ground to free anchor of this entity
     *
     * @param   item
     *          The item to add
     *
     * @effect  The given item is added to a valid anchor of this entity, if such anchor exists
     *          | for each anchor in getAnchorPoints():
     *          |   if canHaveItemAtAnchor(item, anchor)
     *          |   then pickup(item, anchor)
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     * @throws  IllegalArgumentException
     *          The given item does not lie on the ground
     *          | item.getHolder() != null
     * @throws  IllegalArgumentException
     *          There is no valid anchor where the given item can be allocated.
     *          | for each anchor in getAnchorPoints():
     *          |   !canHaveItemAtAnchor(item, anchor)
     * @throws  DeadEntityException
     *          This entity is dead
     *          | isDead()
     * @throws  InvalidHolderException
     *          The given item cannot have this entity as its holder
     *          | !item.canHaveAsHolder(this)
     *
     */
    @Override
    @Raw
    public void pickup(Item item)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException,
            DeadEntityException {
        if(isDead()) throw new DeadEntityException(this);
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!item.canHaveAsHolder(this)) throw new InvalidHolderException(this, item);
        if(!item.liesOnGround()) throw new IllegalArgumentException("The given item doesn't lie on the ground");

        for (Anchorpoint anchor: getAnchorPoints()) {
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
        throw new IllegalArgumentException("No anchor available");
    }

    /**
     * Adds an item to the given anchor
     *
     * @param   item
     *          The item to add
     * @param   anchor
     *          The given to which to add the item
     *
     * @throws  IllegalArgumentException
     *          The given item is not effective.
     *          | item == null
     * @throws  IllegalArgumentException
     *          This entity does not have the given anchor.
     *          | !hasAnchor(anchor)
     * @throws  InvalidAnchorException
     *          The given anchor cannot have the given item.
     *          | !canHaveItemAtAnchor(item, anchor)
     * @throws  InvalidHolderException
     *          The given item cannot have this entity as its holder
     *          | !item.canHaveAsHolder(this)
     *
     */
    @Raw
    public void pickup(Item item, Anchorpoint anchor)
            throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!hasAnchor(anchor)) throw new IllegalArgumentException(this + "has no anchor named: " + anchor);
        if(!item.canHaveAsHolder(this)) throw new InvalidHolderException(this, item);
        if(!canHaveItemAtAnchor(item, anchor)) throw new InvalidAnchorException(this, item, anchor);
        try {
            setAnchor(anchor, item);
            item.setHolder(this);
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
    }

    /*
        Combat
     */

    /**
     *  Variable referencing the intrinsic protection this entity has.
     */
    private final int baseProtection;

    /**
     * Returns the intrinsic protection of this entity
     */
    @Basic @Immutable
    public int getBaseProtection() {
        return baseProtection;
    }

    /**
     * Checks if the given protection is a valid base protection for this entity
     *
     * @param   protection
     *          The protection to check
     *
     * @return  True if and only if the given protection is strictly positive
     *          | result == (protection > 0)
     */
    public boolean canHaveAsBaseProtection(int protection) {
        return protection > 0;
    }

    /**
     * Returns the default protection for this entity
     *
     * @return  The default protection of this entity
     *          | result == ?
     */
    public abstract int getDefaultProtection();

    /**
     * Returns the value that is generated to check if a hit is effective or not.
     */
    protected abstract int getHitChance();

    /**
     * Returns the damage of this entity
     */
    public abstract int getDamage();

    /**
     * Returns the protection of this entity
     */
    public abstract int getProtection();

    /**
     * Hits the given opponent according to the rules of combat
     *
     * @param   opponent
     *          The opponent to hit
     *
     */
    protected void hit(Entity opponent)
            throws IllegalArgumentException, DeadEntityException, InvalidAnchorException, InvalidHolderException, BrokenItemException {
        if(opponent == null) throw new IllegalArgumentException("The given opponent is not effective");
        if(opponent.isDead()) throw new DeadEntityException(opponent);
        if(this.isDead()) throw new DeadEntityException(this);

        startFighting();
        if (getHitChance() > opponent.getProtection()) {
            if (getDamage() > opponent.getHitPoints()) {
                dealFinalBlow(opponent);
            } else {
                opponent.takeDamage(this.getDamage());
            }
        }
        stopFighting();
    }

    protected void dealFinalBlow(Entity opponent) throws InvalidAnchorException, InvalidHolderException, BrokenItemException {
        opponent.die();
        collectTreasuresFrom(opponent);
        for (Item item: opponent.getItems()) {
            if(item != null) item.discard();
        }
    }

    /**
     * Initiates a fight that only ends when this entity or the given opponent dies
     *
     * @param   opponent
     *          The opponent to fight
     *
     * @effect  As long as this entity and the given opponent are not dead, they take turns (starting with this entity)
     *          hitting each other.
     *          | while (!(isDead() || opponent.isDead()))
     *          |   do  hit(opponent)
     *          |       if (!opponent.isDead())
     *          |       then opponent.hit(this)
     * @throws  IllegalArgumentException
     *          The given opponent is not effective
     *          | opponent == null
     */
    protected void fight(Entity opponent) throws IllegalArgumentException {
        if (opponent == null) throw new IllegalArgumentException("THe given opponent is not effective");
        while(!(isDead() || opponent.isDead())) {
            try {
                hit(opponent);
                if(!opponent.isDead()) opponent.hit(this);
            } catch (Exception e) {
                // Should not happen
                assert false;
            }
        }
    }

    /**
     * Collects treasures from the given opponent
     *
     * @param   opponent
     *          The opponent to collect treasures from
     */
    protected abstract void collectTreasuresFrom(Entity opponent) throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException, BrokenItemException;


    /**
     * Moves an item from this entity located at the given anchor
     *
     * @param   recipient
     *          The recipient of the item
     * @param   anchorFrom
     *          The anchor of this entity where the item is located
     * @param   anchorTo
     *          The anchor of the recipient where to add the anchor
     *
     * @effect  The item located at anchorFrom is dropped from this entity
     *          | drop(getItemAt(anchorFrom))
     * @effect  The item at anchorFrom from this entity is added to anchorTo from the recipient
     *          | recipient.setAnchor(anchorTo, (old this).getItemAt(anchorFrom))
     * @throws  IllegalArgumentException
     *          This entity does not have the anchor: anchorFrom
     *          | !this.hasAnchor(anchorFrom)
     * @throws  IllegalArgumentException
     *          The recipient does not have the anchor: anchorTo
     *          | !recipient.hasAnchor(anchorTo)
     * @throws  InvalidHolderException
     *          The recipient cannot pick up the item located at anchorFrom
     *          | !recipient.canPickup(getItemAt(anchorFrom))
     * @throws  InvalidAnchorException
     *          The recipient cannot have the desired item (located at anchorFrom) at the given anchor (anchorTo)
     *          | !recipient.canHaveItemAtAnchor(getItemAt(anchorFrom), anchorTo)
     * @throws  InvalidHolderException
     *          The item cannot have the recipient as its holder
     *          | !getItemAt(anchorFrom).canHaveAsHolder(recipient)
     */
    @Raw
    public void transferItemAtAnchorTo(Entity recipient, Anchorpoint anchorFrom, Anchorpoint anchorTo)
            throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException {
        if(!hasAnchor(anchorFrom))
            throw new IllegalArgumentException(String.format("This entity does not have the anchor: %s", anchorFrom));
        if(!recipient.hasAnchor(anchorTo))
            throw new IllegalArgumentException(String.format("The recipient does not have the anchor: %s", anchorTo));
        if(recipient.getItemAt(anchorTo) != null)
            throw new IllegalArgumentException(String.format("The recipient already holds an item at: %s", anchorTo));
        Item item = getItemAt(anchorFrom);
        if(!recipient.canPickup(item))
            throw new InvalidHolderException(recipient, item);
        if(!recipient.canHaveItemAtAnchor(item, anchorTo))
            throw new InvalidAnchorException(recipient, item, anchorTo);
        if(item != null && !item.canHaveAsHolder(recipient))
            throw new InvalidHolderException(recipient, item);
        try {
            if(item != null) drop(item);
            recipient.pickup(item, anchorTo);
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s[%s]%s:\n♥-%d/%d\n",
                getName(), getClass().getSimpleName(), isDead()? "(Dead)": "", getHitPoints(), getMaxHitPoints());
    }
}
