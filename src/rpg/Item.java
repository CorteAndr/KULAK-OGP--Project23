package rpg;

import be.kuleuven.cs.som.annotate.*;
import rpg.exceptions.*;


/**
 * An abstract class of Items
 *
 * @author  Corteville Andrew
 *
 * @invar   Each item must have a valid id
 *          | canHaveAsId(getId())
 * @invar   Each item must have a valid weight
 *          | isValidWeight(getWeight())
 * @invar   Each item must have a valid value
 *          | canHaveAsValue(getValue())
 * @invar   Each item must have a valid holder
 *          | hasProperHolder()
 */
public abstract class Item {
    /*
        Constructors
     */
    /**
     * Initializes this Item with the given id, weight and value.
     *
     * @param   id
     *          The identification for the new item
     * @param   weight
     *          The weight of the new item
     * @param   value
     *          The value of the new item
     *
     * @post    The identification of this new item is set to the given id.
     *          | new.getId() == id
     * @post    The weight of this new item is set to the given weight.
     *          | new.getWeight() == weight
     * @effect  The value of this new item is set to the given value.
     *          | new.getValue() == value
     */
    protected Item(long id, double weight, int value) {
        if(!canHaveAsId(id)) id = getValidId();
        if(!isValidWeight(weight)) weight = getDefaultWeight();

        this.id = id;
        this.weight = weight;
        if(!canHaveAsValue(value)) value = getDefaultValue();
        try {
            setValue(value);
        } catch (Exception e) {
            //Should not happen
            assert false;
        }
    }

    /*
        Destructors
     */
    /**
     * Variable referencing whether is item is broken.
     */
    private boolean isBroken = false;

    /**
     * Returns true if and only if this item was destroyed.
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * Destroys this item indefinitely.
     *
     * @effect  The current holder drops this item on the ground
     *          | getHolder().dropItem(this)
     * @post    Sets the isBroken attribute of this item to true
     *          | new.IsBroken() == true
     * @throws BrokenItemException
     *          Item is already broken
     *          | isBroken()
     */
    protected void destroy() throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        this.isBroken = true;
    }

    /**
     * Destroys this item and drops it from the holder's inventory
     *
     * @effect  This piece of item is destroyed
     *          | destroy()
     * @effect  If the holder of this item is effective then said holder drops this item
     *          | if(getHolder() != null)
     *          | then getHolder.drop(this)
     */
    public void discard() throws BrokenItemException {
        if(getHolder() != null) getHolder().drop(this);
        destroy();
    }

    /*
        Identification (TOTAL)
     */
    /**
     * Variable referencing the identification number of the item.
     */
    private final long id;

    /**
     * Returns the identification of the item.
     */
    @Basic
    @Raw
    @Immutable
    public long getId() {
        return id;
    }

    /**
     * Returns a validID to be used.
     */
    protected abstract long getValidId();

    /**
     * Checks if this item can have the given identification number as id
     *
     * @param   id
     *          The identification number to check.
     * @return  True always
     *          | result == true
     */
    public boolean canHaveAsId(long id) {
        return true;
    }

    /*
        Weight (TOTAL)
     */

    /**
     * Variable referencing the weight of the item expressed in kg.
     */
    private final double weight;

    /**
     * Returns the weight of the item
     */
    @Basic @Raw @Immutable
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the default weight value
     */
    public static double getDefaultWeight() {
        return 15.00;
    }

    /**
     * Checks if the given weight is a valid weight.
     *
     * @param   weight
     *          The weight to check
     * @return  True if and only if the given weight is a positive number
     *          | result == (weight >= 0.00)
     */
    public static boolean isValidWeight(double weight) {
        return weight >= 0.00;
    }

    /*
        Value(DEFENSIVE)
     */
    /**
     * Variable referencing the value of this item.
     */
    private int value = 0;

    /**
     * Returns the value of this item.
     */
    @Basic @Raw
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this item to the given value
     *
     * @param   value
     *          The given value
     * @post    The new value of this item is the given value
     *          | new.getValue() == value
     * @throws  IllegalArgumentException
     *          The given value is not valid for this item
     *          | !canHaveAsValue(value)
     */
    @Model
    @Raw
    protected void setValue(int value) throws IllegalArgumentException {
        if (!canHaveAsValue(value)) throw new IllegalArgumentException(value + "is not a valid value");
        this.value = value;
    }

    /**
     * Checks if the given value is a valid value
     *
     * @param   value
     *          The value to check
     * @return  True if and only if the given value if a positive integer.
     *          | result == (value >= 0)
     */
    public boolean canHaveAsValue(int value) {
        return value >= 0;
    }

    /**
     * Returns a default value for an item if the given item was not valid;
     *  | canHaveAsValue(result)
     */
    public int getDefaultValue() {
        return 0;
    }

    /*
        Holder  (DEFENSIVE)
     */
    /**
     * Variable referencing the holder of this item;
     */
    private ItemHolder holder = null;


    /**
     * Returns the holder of this item.
     */
    @Basic
    public ItemHolder getHolder() {
        return holder;
    }

    /**
     * Returns the highest holder of this item
     *
     * @return  The highest holder of this item, meaning that if the holder of this item is a backpack than return the
     *          highest holder of said backpack.
     *          | if(getHolder() != null && getHolder() is instance of Backpack)
     *          | then result == ((BackPack) getHolder()).getHighestHolder()
     *          | else result == getHolder()
     */
    public ItemHolder getHighestHolder() {
        if(getHolder() != null && getHolder().getClass() == Backpack.class) {
            if (((Backpack) getHolder()).getHolder() == null) {
                return getHolder();
            } else {
                return ((Backpack) getHolder()).getHighestHolder();
            }
        }
        return getHolder();
    }

    /**
     * @return  True if and only if the given item lies on the ground (does not have an effective holder)
     *          | result == (getHolder() == null)
     */
    public boolean liesOnGround() {
        return getHolder() == null;
    }

    /**
     * Sets the holder of this item to the given ItemHolder.
     *
     * @param   holder
     *          The new ItemHolder
     * @post    The holder of this item is set to the given holder
     *          | new.getHolder() == holder
     */
    @Model
    protected void setHolder(@Raw ItemHolder holder) {
        this.holder = holder;
    }


    /**
     * Checks if the given holder can hold this item.
     *
     * @param   holder
     *          The given holder
     *
     * @return  If this item is broken return true if and only if the given holder is not effective.
     *          Otherwise, return true if and only if the given holder is not effective, the current holder or
     *          can pick up this item.
     *          | if (isBroken())
     *          | then result == ( holder == null )
     *          | else result == (
     *          |                   holder == null ||
     *          |                   holder == getHolder() ||
     *          |                   holder.canPickup(this)
     *          |               )
     */
    public boolean canHaveAsHolder(ItemHolder holder) {
        if(isBroken()) {
            return holder == null;
        } else {
            return (holder == null) || holder == getHolder() || holder.canPickup(this);
        }
    }

    /**
     * Checks if this item has a proper holder as its holder
     * @return True if:
     *          - The current holder is a valid holder, and
     *          - This item either lies on the ground or its holder references this item in its contents,
     *          false otherwise
     *          | result ==
     *          | canHaveAsHolder(getHolder()) &&
     *          | ( liesOnGround() || getHolder().holdsItemDirectly(this) )
     */
    public boolean hasProperHolder() {
        return canHaveAsHolder(getHolder()) && (liesOnGround() || getHolder().holdsItemDirectly(this));
    }

    /*
        Shiny
     */

    /**
     * Returns how shiny this item is.
     */
    public abstract int getShiny();


    @Override
    public String toString() {
        return String.format("%s -> total weight: %.2f, total value: %d", getClass().getSimpleName(), getWeight(), getValue());
    }
}