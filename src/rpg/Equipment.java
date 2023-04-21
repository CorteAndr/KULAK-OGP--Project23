package rpg;

import be.kuleuven.cs.som.annotate.*;
import rpg.exceptions.BrokenEquipmentException;
import rpg.exceptions.InvalidHolderException;

/**
 * A class of Equipments
 *
 * @author  Corteville Andrew
 *
 * @invar   Each equipment has a valid id
 *          | isValidId(getId())
 * @invar   Each equipment has a valid weight
 *          | isValidWeight(getWeight())
 * @invar   Each equipment should have a valid value
 *          | isValidValue(getValue())
 * @invar   Each equipment should have a validHolder
 *          | isValidHolder(getHolder())
 */
public abstract class Equipment {
    /*
        Constructors
     */

    /**
     * Initializes this new Equipment with an identification number, weight, value and holder
     *
     * @param   id
     *          The identification number
     * @param   weight
     *          The weight of the item
     * @param   value
     *          The (base)value of the item
     * @param   holder
     *          The EquipmentHolder that owns this Equipment.
     * @post    The identification is set to the given identification number or a valid id if the given id was not valid
     *          | new.getId() == id
     * @post    The weight of the new equipment is set to the given weight or
     *          a default weight if the given weight was invalid
     *          | if(isValidWeight(weight))
     *          | then
     *          | new.getWeight() == weight
     *          | else
     *          | new.getWeight() == getDefaultWeight();
     * @effect  The value of this equipment is set to the given value
     *          | setValue(value)
     * @effect  The holder of this equipment is set to the given holder
     *          | setHolder(holder)
     */
    @Raw
    public Equipment(long id, double weight, int value, EquipmentHolder holder)
            throws IllegalArgumentException, InvalidHolderException, BrokenEquipmentException {

        if(!isValidId(id)) id = getValidId();
        if(!isValidWeight(weight)) weight = getDefaultWeight();

        this.id = id;
        this.weight = weight;
        setValue(value);
        setHolder(holder);

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
     * @throws BrokenEquipmentException
     *          Equipment is already broken
     *          | isBroken()
     */
    protected void destroy() throws BrokenEquipmentException {
        if(isBroken()) throw new BrokenEquipmentException(this);
        getHolder().drop(this);
        this.isBroken = true;
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
    @Basic @Raw
    public long getId() {
        return id;
    }

    /**
     * Returns a validID to be used.
     */
    protected abstract long getValidId();

    /**
     * Checks if the given identification number is valid
     *
     * @param   id
     *          The identification number to check.
     * @return  True always
     *          | result == true
     */
    public static boolean isValidId(long id) {
        return true;
    }

    /*
        Weight (TOTAL)
     */

    /**
     * Variable referencing the weight of the item.
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
    @Model
    private static double getDefaultWeight() {
        return 15.00;
    }

    /**
     * Checks if the given weight is a valid weight.
     *
     * @param   weight
     *          The weight to check
     * @return  True if and only if the given weight is a strictly positive number
     *          | result == (weight > 0.00)
     */
    public static boolean isValidWeight(double weight) {
        return weight > 0.00;
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
     */
    @Model
    protected void setValue(int value) throws IllegalArgumentException, BrokenEquipmentException {
        if (!isValidValue(value)) throw new IllegalArgumentException(value + "is not a valid value");
        if (isBroken()) throw new BrokenEquipmentException(this);
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
    public static boolean isValidValue(int value) {
        return value >= 0;
    }

    /*
        Holder  (DEFENSIVE)
     */
    /**
     * Variable referencing the holder of this item;
     */
    private EquipmentHolder holder = null;


    /**
     * Returns the holder of this item.
     */
    @Basic
    public EquipmentHolder getHolder() {
        return holder;
    }

    /**
     * Sets the holder of this item to the given EquipmentHolder.
     *
     * @param   holder
     *          The new EquipmentHolder
     * @post    The holder of this item is set to the given holder
     *          | new.getHolder() == holder
     * @throws  IllegalArgumentException
     *          If the given holder is not a valid holder
     *          | !isValidHolder(holder)
     * @throws BrokenEquipmentException
     *          This item is broken
     *          | isBroken()
     */
    @Model
    protected void setHolder(EquipmentHolder holder) throws InvalidHolderException, BrokenEquipmentException {
        if(!isValidHolder(holder)) throw new InvalidHolderException(holder, this);
        if(isBroken()) throw new BrokenEquipmentException(this);

        this.holder = holder;
    }


    /**
     * Checks if the given holder can hold this equipment.
     *
     * @param   holder
     *          The given holder
     * @return  True if and only if the given holder is not effective or alive and can pick up this item.
     *          | result ==
     *          |   (holder == null) ||
     *          |   (holder.canPickupItem(this))
     */
    public boolean isValidHolder(EquipmentHolder holder) {
        if(isBroken()) return holder == null;
        return (holder == null) || (holder.canPickup(this));
    }

    /*
        Shiny
     */

    /**
     * Returns how shiny this item is.
     */
    public abstract int getShiny();

}