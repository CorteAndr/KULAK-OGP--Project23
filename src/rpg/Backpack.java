package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.*;


/**
 * A class of Backpacks
 *
 * @author  Corteville Andrew
 *
 * @invar   Each backpack should have proper contents
 *          | hasProperContents()
 */
public class Backpack extends Storage implements ItemHolder {

    /**
     * Initializes this new Backpack with a currently available id, the given weight, value, holder and capacity.
     *
     * @param   weight
     *          The given weight
     * @param   value
     *          The given value
     * @param   holder
     *          The given holder
     * @param   capacity
     *          The given capacity
     * @effect  Initializes this backpack with a generated ID, the given weight, value, holder and capacity
     *          | super(getNextId(), weight, value, holder, capacity)
     * @post    Initializes the contents of this backpack as an empty Hashmap
     *          | new.getContents() == new HashMap<>()
     */
     public Backpack(double weight, int value, ItemHolder holder, double capacity)
             throws BrokenItemException, InvalidHolderException {

         super(getNextId(), weight, value);
         if(!isValidCapacity(capacity)) capacity = getDefaultCapacity();
         this.capacity = capacity;
         setHolder(holder);

     }
     /*
        Identification (TOTAL)
      */

    /**
     * Variable referencing a currently available ID
     */
    private static long currentId = Long.MIN_VALUE;

    /**
     * @return  The currently available ID.
     */
    public static long getCurrentId() {
        return currentId;
    }

    /**
     * @effect  Returns the currently available id and increments it.
     *          | getNextId()
     */
    @Override
    protected long getValidId() {
        return getNextId();
    }

    @Override
    public boolean canHaveAsId(long id) {
        return true;
    }
    /**
     * Returns the currently available id and increments it.
     * @post    currentId is incremented with 1.
     *          | old.getCurrentId() + 1 == new.getCurrentId()
     */
    private static long getNextId() {
        return currentId++;
    }


    /*
        Capacity (TOTAL)
     */

    /**
     * Variable referencing the maximum capacity of this backpack.
     */
    private final double capacity;

    /**
     * Checks if the given capacity is a valid capacity for a backpack
     *
     * @param   capacity
     *          The capacity to check
     * @return  True if and only if the given capacity is a positive number
     *          | result == (capacity >= 0.00)
     */
    public static boolean isValidCapacity(double capacity) {
        return capacity >= 0.00;
    }

    /**
     * @return  the capacity of this backpack
     */
    @Basic @Immutable
    public double getCapacity() {
        return capacity;
    }

    /**
     * @return  The default capacity of a backpack
     */
    @Model
    private static double getDefaultCapacity() {
        return 0.00;
    }

    /*
        Content (DEFENSIVE)
     */
    /**
     * Variable referencing the contents of this backpack
     *
     * @invar   contents references an effective map
     *          | contents != null
     * @invar   Each mapping references a valid list
     *          | for each key, list in contents:
     *          | list != null
     * @invar   Each element in each mapping references a non-broken item
     *          | for each key, list in contents:
     *          |   for each item in list:
     *          |       !item.isBroken()
     * @invar   Each element in each mapping has the same id as the key of the mapping
     *          | for each key, list in contents:
     *          |   for each item in list:
     *          |       item.getId() == key
     * @invar   Each element in each mapped list references an item that references back to this backpack
     *          | for each key, list in contents:
     *          |   for each item in list:
     *          |       item.getHolder() == this
     *
     */
    private final Map<Long, ArrayList<Item>> contents = new HashMap<>();

    /**
     * @return  A set of identifications that are stored inside this backpack
     */
    @Basic
    public Set<Long> getStoredIds() {
        return contents.keySet();
    }

    /**
     * Returns how many items within this backpack has the given id.
     * @param   id
     *          The id to search
     * @return  The number of items located inside this backpack with the given id.
     *          | if (!contents.containsKey(id)) result == 0
     *          | else return contents.get(id).size()
     */
    @Basic
    public int getNbItemsWithId(long id) {
        if(!contents.containsKey(id)) return 0;
        try {
            return contents.get(id).size();
        } catch (NullPointerException e) {
            // Should not happen
            assert false;
        }
        // Should not happen
        return -1;
    }

    /**
     * Returns the list of every item located within this backpack that has the given id.
     *
     * @param   id
     *          The given id
     * @return  A list of items that are located inside this backpack and have the given id
     *          | result == contents.get(id)
     * @throws  IllegalArgumentException
     *          This backpack does not contain any items with the given id
     *          | if(getNbItemsWithId(id) == 0)
     */
    private List<Item> getItemsWithId(long id) {
        if(getNbItemsWithId(id) == 0)
            throw new IllegalArgumentException("This backpack does not contains any items with the given id");
        return contents.get(id);
    }

    /**
     * Returns the item with the given id at the given position
     *
     * @param   id
     *          The id of the desired item
     * @param   pos
     *          The position of the desired item.
     * @return  The item within this backpack with the given id and at the given position within the items with that id.
     *          | result == ( contents.get(id).get(pos) )
     * @throws  IllegalArgumentException
     *          The given id is not located anywhere inside this backpack
     *          | getNbItemsWithId(id) == 0
     * @throws  IndexOutOfBoundsException
     *          The given position exceeds the number of items with the given id that this backpack holds
     *          | pos >= getNbItemsWithId(id)
     */
    @Basic
    public Item getItemWithIdAtPos(long id, int pos) throws IllegalArgumentException, IndexOutOfBoundsException {
        if(getNbItemsWithId(id) == 0)
            throw new IllegalArgumentException("This backpack does not contains any items with the given id");
        if(getNbItemsWithId(id) <= pos) throw new IndexOutOfBoundsException("Index out of bounds: "+pos);
        try {
            return getItemsWithId(id).get(pos);
        } catch(NullPointerException | IndexOutOfBoundsException | IllegalArgumentException e) {
            // Should not happen
            assert false;
        }
        // Should not happen
        return null;
    }

    /**
     * Checks if the given item exists within this backpack
     *
     * @return  True if and only if the given item exists within this backpack
     *          | result == ( !(getNbItemsWithId(item.getId()) == 0) &&
     *          |( getItemsWithId(item.getId()).contains(item) )
     */
    @Override
    public boolean holdsItemDirectly(Item item) throws IllegalArgumentException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(getNbItemsWithId(item.getId()) == 0) return false;
        try {
            return getItemsWithId(item.getId()).contains(item);
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
        return false;
    }

    /**
     * Checks if the given item is held (directly or indirectly) by this backpack
     *
     * @param   item
     *          The given item
     * @return  True if and only if this backpack holds the given item either directly or indirectly
     *          | result == (
     *          |   item.getHolder() == this || (
     *          |       item.getHolder() instance of Backpack &&
     *          |       this.holdsItem((Backpack) item.getHolder())
     *          |       )
     *          |   )
     */
    @Override
    public boolean holdsItem(Item item) {
        ItemHolder itemHolder = item.getHolder();
        while(itemHolder != null && itemHolder != this) {
            if (itemHolder.getClass() == Backpack.class) {
                holdsItem((Backpack) item.getHolder());
            } else {
                return false;
            }
        }
        return itemHolder == this;
    }

    /**
     * Returns how many items of the given type are held, directly and indirectly by this backpack
     *
     * @param   type
     *          The type to search
     * @return  The number of items that are held directly or indirectly by this backpack of the given type.
     *          | result == (
     *          |
     */
    //TODO documentation
    @Override
    public int getNbOfItemsOfTypeHeld(Class<? extends Item> type) {
        int amount = 0;
        for (ArrayList<Item> items: contents.values()) {
            for (Item item: items) {
                if (item.getClass() == type) amount++;
                if (item.getClass() == Backpack.class) amount += ((Backpack) item).getNbOfItemsOfTypeHeld(type);
            }
        }
        return amount;
    }

    /**
     * Checks if this backpack has proper contents
     *
     * @return  True if and only if this backpack can have all of its items at their respective mapping and can hold
     *          it
     *          | result ==
     *          | for each key, mapping in contents:
     *          |   for each item in mapping:
     *          |       (canPickUp(item) && item.getHolder() == this && item.getId() == key)
     */
    @Raw
    public boolean hasProperContents() {
        for (Map.Entry<Long, ArrayList<Item>> entry: contents.entrySet()) {
            for(Item item: entry.getValue()) {
                if (!canPickup(item) || item.getHolder() != this || item.getId() != entry.getKey()) return false;
            }
        }
        return true;
    }

    /**
     * Returns the total weight of all items located in this backpack
     *
     * @return  The total weight of the equipment in the contents of this backpack
     *          |
     */
    @Override
    public double getLoad() {
        double load = 0.00;
        for (Map.Entry<Long, ArrayList<Item>> entry: contents.entrySet()) {
            for (Item eq: entry.getValue()) {
                load += eq.getWeight();
            }
        }
        return load;
    }

    /*
        Value
     */

    /**
     * @return  The value intrinsic to this backpack
     */
    @Basic
    public int getOwnValue() {
        return super.getValue();
    }

    /**
     * @return  The cumulative value of all equipment located in this backpack
     */
    public int getLoadValue() {
        int value = 0;
        for (Map.Entry<Long, ArrayList<Item>> entry: contents.entrySet()) {
            for (Item eq: entry.getValue()) {
                value += eq.getValue();
            }
        }
        return value;
    }


    /**
     * Returns the total value of this backpack, as in the value of itself and its contents.
     *
     * @return  The sum of its own value and the value of the contents of this backpack
     *          | result == getOwnValue() + getLoadValue()
     */
    @Override
    @Basic
    public int getValue() {
        return getOwnValue() + getLoadValue();
    }

    /**
     * Checks if the given value is a valid value for a backpack
     *
     * @param   value
     *          The value to check
     * @return  True if and only the given value is a positive integer that is less than or equal to the maximum value
     *          a backpack can have
     *          | result ==
     *          |   ( 0 <= value ) &&
     *          |   ( value <= getMaxValue() )
     */
    @Override
    public boolean canHaveAsValue(int value) {
        return (0 <= value) && (value <= getMaxValue());
    }

    /**
     * @return  The maximum value a backpack can have.
     */
    public static int getMaxValue() {
        return 500;
    }

    /*
        Item Holder implementation
     */

    /**
     * Checks if the given item can be added to this backpack
     *
     * @param   item
     *          The item to check
     *
     * @return  True if and only if the given item not broken and either already is inside this backpack or
     *          this backpack can pick up the weight of the given item, otherwise return false.
     *          | result ==
     *          | ( !item.isBroken() ) &&
     *          | ( (containsItem(item) ) ||
     *          | ( canPickup(item.getWeight() ) )
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     */
    @Override
    public boolean canPickup(Item item) throws IllegalArgumentException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        return !item.isBroken() && (holdsItemDirectly(item) || canPickup(item.getWeight()));
    }

    /**
     * Checks if the given weight can be added to this backpack
     *
     * @param   weight
     *          The given weight
     *
     * @return  True if and only if the weight added to the current load doesn't exceed the capacity of this backpack.
     *          Otherwise, return false.
     *          | result == getLoad() + weight <= getCapacity
     * @throws  IllegalArgumentException
     *          The given weight is negative
     *          | weight < 0
     */
    @Override
    public boolean canPickup(double weight) throws IllegalArgumentException {
        if(weight < 0) throw new IllegalArgumentException("The given weight is negative");
        return getLoad() + weight <= getCapacity();
    }

    /**
     * Removes the given item from its contents and makes the holder of the given item not effective.
     *
     * @param   item
     *          The item to remove
     * @post    Removes the given item from the contents
     *          | !holdsItem(this) )
     * @effect  The new holder of the given item is set to be not effective
     *          | item.setHolder(null)
     * @throws  IllegalArgumentException
     *          If the given item is not directly located inside the contents of this backpack
     *          | !holdsItemDirectly(item)
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     */
    @Override
    @Raw
    public void drop(Item item)
            throws IllegalArgumentException {

        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if( !holdsItemDirectly(item))
            throw new IllegalArgumentException("Item is not located inside the contents of this backpack");

        contents.get(item.getId()).remove(item);
        try {
            item.setHolder(null);
        } catch(Exception e) {
            assert false;
        }
    }

    /**
     * Picks up an item from the ground by adding it to its contents and changing the holder of the item to this
     * backpack
     *
     * @param   item
     *          The item to pick up
     *
     * @post    The item is added to the contents of this backpack
     *          | new.getContents().get(item.getId()).contains(item)
     * @effect  The holder of the given item is set to this backpack
     *          | item.setHolder(this)
     * @effect  If the given item already has an effective holder, then said holder drops this item
     *          | if (item.getHolder() != null)
     *          | then
     *          | item.getHolder().drop(item)
     * @throws  IllegalArgumentException
     *          The given item is not effective
     *          | item == null
     * @throws  IllegalArgumentException
     *          The given item cannot be picked up
     *          | !canPickup(item)
     * @throws  IllegalArgumentException
     *          The given item doesn't lie on the ground
     *          | !item.liesOnGround()
     * @throws  InvalidHolderException
     *          This backpack is not a valid holder for the given item
     *          | !item.canHaveAsHolder(this)
     */
    @Override
    @Raw
    public void pickup(Item item)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException {
        if(item == null) throw new IllegalArgumentException("The given item is not effective");
        if(!item.canHaveAsHolder(this)) throw new InvalidHolderException(this, item);
        if(!canPickup(item)) throw new IllegalArgumentException("Cannot pickup this Item");
        if(!item.liesOnGround()) throw new IllegalArgumentException("You can only take items that are on the ground");
        try {
            item.setHolder(this);
        } catch (InvalidHolderException e) {
            // Should not happen
            assert false;
        }

        if(getNbItemsWithId(item.getId()) == 0) contents.put(item.getId(), new ArrayList<>());
        contents.get(item.getId()).add(item);
    }

}
