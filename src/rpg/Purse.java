package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import rpg.exceptions.BrokenItemException;

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
 * @note    Was not required
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
     *
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
        this.capacity = capacity;
        if(!canHaveAsContents(contents)) {
            contents = getDefaultContents();
        }
        try {
            setContents(contents);
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
     * @effect  Initializes a new Purse with a generated identification, the given weight and capacity and no contents
     *          | this(generateId(), weight, capacity, 0)
     */
    public Purse(double weight, int capacity) {
        this(generateId(), weight, capacity, 0);
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
    private void setContents(int contents) throws IllegalArgumentException, BrokenItemException {
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
     *
     * @throws  IllegalArgumentException
     *          The given amounts is not a strictly positive integer
     *          | amount <= 0
     * @throws  IllegalArgumentException
     *          If the contents of this Purse can be increased with the given amount and this purse lies on the ground and
     *          its holder cannot pick up the added weight of ducats
     *          | canHaveAsContents(getContents() + amount) && !liesOnGround() && !getHolder().canPickup(amount*ducatWeight)
     * @effect  If the contents of this Purse can be increased with the given amount then the contents of this Purse are
     *          increased with the given amount
     *          | if(canHaveAsContents(getContents() + amount)
     *          | then setContents(getContents() + amount)
     * @effect  If the contents of this Purse can be increased with the given amount then the value of this Purse is
     *          increased with the given amount
     *          | if(canHaveAsContents(getContents() + amount)
     *          | then setValue(getValue() + amount)
     * @effect  If the contents of this Purse cannot be increased with the given amount then the contents of this Purse
     *          are set to 0.
     *          | if(!canHaveAsContents(getContents() + amount)
     *          | then setContents(0)
     * @effect  If the contents of this Purse cannot be increased with the given amount then the value of this Purse
     *          is set to 0.
     *          | if(!canHaveAsContents(getContents() + amount)
     *          | then setValue(0)
     * @effect  If the contents of this Purse cannot be increased with the given amount then this Purse is destroyed.
     *          | if(!canHaveAsContents(getContents() + amount)
     *          | then destroy()
     */
    public void addDucats(int amount) throws BrokenItemException {
        if(amount <= 0) throw new IllegalArgumentException("The amount of ducats to add should be strictly positive");
        if(canHaveAsContents(getContents() + amount)) {
            if(!liesOnGround() && !getHolder().canPickup(amount * getDucatWeight()))
                throw new IllegalArgumentException("The holder of this purse cannot have the given weight");
            setContents(getContents() + amount);
            setValue(getValue() + amount);
        } else {
            setContents(0);
            setValue(0);
            destroy();
        }
    }

    /**
     * Removes a number of ducats from this purse.
     *
     * @param   amount
     *          The amount of ducats to remove
     *
     * @effect  The contents of this purse is set to the old contents decreased with the given amount
     *          | setContents(getContents() - amount)
     *
     * @throws  IllegalArgumentException
     *          If the given amount is not strictly positive
     *          | amount <= 0
     * @throws  IllegalArgumentException
     *          If the given amount exceeds the contents of this Purse
     *          | getContents() < amount
     */
    public void removeDucats(int amount)
            throws IllegalArgumentException, BrokenItemException {
        if(amount <= 0) throw new IllegalArgumentException("The amount of ducats to remove should be strictly positive");
        if(getContents() < amount)
            throw new IllegalArgumentException("The amount of ducats to remove should less than or equal the current contents of this purse");
        setContents(getContents() - amount);

    }

    /**
     *  Transfers all ducats inside this purse to the given purse and drops this purse.
     *
     * @param   other
     *          The other purse to transfer to
     *
     * @effect  Transfers the all contents of this purse to the given other purse
     *          | transferDucatsTo(other, getContents())
     * @effect  If this purse does not lie on the ground then the holder of this purse drops this purse.
     *          | if(!liesOnGround)
     *          | then getHolder().drop(this)
     */
    public void transferAllDucatsTo(Purse other)
            throws IllegalArgumentException, BrokenItemException {
        transferSomeDucatsTo(other, getContents());
        if(!liesOnGround()) getHolder().drop(this);
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
            throws IllegalArgumentException, BrokenItemException {
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
     *
     * @note    Removes isBroken() check
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
     *
     * @note    Makes destroying possible without discarding it
     */
    public void destroy() throws BrokenItemException {
        super.destroy();
    }
}
