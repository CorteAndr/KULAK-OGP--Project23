package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidHolderException;

/**
 * A class of purses
 *
 * @author  Corteville Andrew
 *
 * @invar   Each purse has a valid capacity
 *          | isValidCapacity(getCapacity())
 * @invar   Each purse has valid contents
 *          | isValidContents(getContents())
 *
 * @note    Not a requirement but implemented and unfinished
 */
public class Purse extends Storage {

    /**
     * Initializes this purse with the given id, weight, holder, capacity and contents alongside the value equal to
     * the contents
     *
     * @param   id
     *          The given id
     * @param   weight
     *          The given weight
     * @param   capacity
     *          The given capacity
     * @param   contents
     *          The given contents
     * @effect  Initializes this purse with the given id, weight and no value.
     *          | super(id, weight, 0)
     * @post    If the given capacity is a valid capacity then the capacity of this purse is set to the given capacity
     *          Otherwise it is set to the default capacity of a purse
     *          | if(isValidCapacity(capacity)
     *          | then
     *          | new.getCapacity() == capacity
     *          | else
     *          | new.getCapacity() == getDefaultCapacity()
     * @effect  The contents of this purse are set to the given contents or the default contents if the given contents
     *          are not valid
     *          | if(canHaveAsContents(contents)
     *          | then
     *          | new.getContents() == contents
     *          | else
     *          | new.getContents() == getDefaultContents()
     * @effect  The value of this new purse is set to its contents
     *          | setValue(getContents())
     */
    public Purse(long id, double weight, int capacity, int contents) {
        super(id, weight, 0);
        if(!isValidCapacity(capacity)) capacity = getDefaultCapacity();
        if(!canHaveAsContents(contents)) {
            contents = getDefaultContents();
        }
        this.capacity = capacity;
        try {
            setContent(contents);
        } catch (Exception e) {
            //Should not happen
            assert false;
        }
        setValue(getContents());
    }

    /**
     * Initializes a new purse with the given weight and capacity
     *
     * @param   weight
     *          The weight of the new Purse
     * @param   capacity
     *          The capacity of the new Purse
     *
     * @effect
     */
    //TODO
    public Purse(double weight, int capacity) {
        this(generateId(), weight, capacity, getDefaultContents());
    }


    /**
     * @return  A valid identification
     */
    @Override
    protected long getValidId() {
        return 0L;
    }

    /**
     * @return  A valid identification for a purse
     *          | canHaveAsId(result)
     */
    private static long generateId() {
        return 0L;
    }

    /*
        Capacity
     */
    /**
     * Variable referencing the maximum number of ducats that can be inside this purse
     */
    private final int capacity;

    /**
     * @return  The maximum capacity of this purse
     */
    @Basic @Immutable
    public int getCapacity() {
        return capacity;
    }

    /**
     * Checks if the given capacity is a valid capacity of a Purse
     *
     * @param   capacity
     *          The capacity to check
     * @return  True if and only if the given capacity is a positive integer
     *          | result == ( capacity >= 0 )
     */
    public static boolean isValidCapacity(int capacity) {
        return capacity >= 0;
    }

    /**
     * @return  The default capacity of a purse
     */
    @Model
    private static int getDefaultCapacity() {
        return 500;
    }

    /*
        Contents
     */

    /**
     * Variable referencing how many ducats are inside this purse
     */
    private int contents = 0;

    /**
     * @return  How many ducats are inside this purse
     */
    @Basic
    public int getContents() {
        return contents;
    }

    /**
     * Sets the contents to the given contents
     *
     * @param   contents
     *          The contents to set to
     * @post    Sets the contents to the given contents
     *          | new.getContents() == contents
     * @throws  IllegalArgumentException
     *          The given contents are not valid contents for a purse
     *          | !isValidContents(contents)
     * @throws BrokenItemException
     *          This purse is broken
     *          | isBroken()
     */
    @Model
    private void setContent(int contents) throws IllegalArgumentException, BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        if(!canHaveAsContents(contents)) throw new IllegalArgumentException("Invalid contents for a purse");
        this.contents = contents;
        setValue(contents);
    }

    /**
     * Adds a given amount of ducats to the given contents
     *
     * @param   amount
     *          The given amount of ducats to add
     * @pre     amount should be a strictly positive integer
     *
     */
    public void addDucats(int amount) throws BrokenItemException {
        if(amount <= 0) throw new IllegalArgumentException("The amount of ducats to add should be strictly positive");
        if(canHaveAsContents(getContents() + amount)) {
            if(getHolder() != null && getHolder().canPickup(amount * getDucatWeight()))
                throw new IllegalArgumentException("The holder of this purse cannot have the given weight");
            setContent(getContents() + amount);
            setValue(getContents());
        } else {
            setContent(0);
            setValue(0);
            destroy();
        }
    }

    /**
     * Removes a number of ducats from this purse.
     *
     * @param   amount
     *          The amount of ducats to remove
     * @effect  The contents of this purse is set to the old contents decreased with the given amount
     *          | setContents(getContents() - amount)
     * @throws  IllegalArgumentException
     *          If the given amount is not strictly positive or exceeds the current contents of the purse.
     */
    public void removeDucats(int amount)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException {
        if(0 <= amount || getContents() < amount)
            throw new IllegalArgumentException("The amount of ducats to remove should be strictly positive and less " +
                    "than or equal the current contents of this purse");
        if(getContents() - amount == 0 && getHolder() != null) getHolder().drop(this);
        setContent(getContents() - amount);

    }

    /**
     *  Transfers all ducats inside this purse to the given purse and drops this purse.
     *
     * @param   other
     *          The other purse to transfer to
     * @effect  Transfers the all contents of this purse to the given other purse
     *          | transferDucatsTo(other, getContents())
     * @effect  Drops this purse on the ground if it wasn't already on the ground
     *          | if(getHolder != null)
     *          | then
     *          | getHolder.drop()
     */
    public void transferAllDucatsTo(Purse other)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException {
        transferSomeDucatsTo(other, getContents());
    }

    /**
     * Transfers an amount of ducats to another purse
     *
     * @param   other
     *          The other purse to transfer to
     * @param   amount
     *          The amount of ducats to transfer
     * @effect  Removes the given amount from this purse
     *          | removeDucats(amount)
     * @effect  Adds the given amount of ducats to the other purse
     *          | other.addDucats(amount)
     */
    public void transferSomeDucatsTo(Purse other, int amount)
            throws IllegalArgumentException, BrokenItemException, InvalidHolderException {
        removeDucats(amount);
        other.addDucats(amount);
    }

    /**
     * Checks if the given contents is valid for this purse
     *
     * @param   contents
     *          The contents to check
     * @return  True if and only if the given contents is a positive integer that is less than or equal to the capacity
     *          of this purse
     *          | result ==
     *          |   ( 0 <= contents) &&
     *          |   ( contents <= getCapacity() )
     */
    public boolean canHaveAsContents(int contents) {
        return (0 <= contents && contents <= getCapacity());
    }

    /**
     * @return  The default contents of a purse
     */
    @Model
    private static int getDefaultContents() {
        return 0;
    }

    /**
     * @return  The weight of the ducats inside this purse
     */
    @Override @Basic
    public double getLoad() {
        return getContents() * getDucatWeight();
    }

    /**
     * Checks if the given holder can be a holder
     * @param   holder
     *          The holder to check
     *
     * @return  True if and only if this purse lies on the ground or this holder or the holder can pick up this purse
     *          | result == (
     *          |   liesOnGround() ||
     *          |   getHolder() == holder ||
     *          |   holder.canPickup(this)
     *          | )
     */
    @Override
    public boolean canHaveAsHolder(ItemHolder holder) {
        return holder == null || (getHolder() == holder || holder.canPickup(this));
    }

    /**
     * Variable referencing the weight of a single ducat
     */
    private static final double ducatWeight = 0.05;

    /**
     * @return  The weight of a singular ducat expressed in kg.
     */
    @Basic @Immutable
    public static double getDucatWeight() {
        return ducatWeight;
    }

    /**
     * Destroys this item without discarding it
     *
     * @effect  Destroys this item
     *          | super.destroy()
     */
    public void destroy() throws BrokenItemException {
        super.destroy();
    }
}
