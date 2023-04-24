package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import rpg.exceptions.BrokenEquipmentException;
import rpg.exceptions.InvalidHolderException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * A class of Backpacks
 *
 * @author  Corteville Andrew
 *
 * @invar
 */
public class Backpack extends Storage implements EquipmentHolder {

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
     public Backpack(double weight, int value, EquipmentHolder holder, double capacity)
             throws BrokenEquipmentException, InvalidHolderException {

         super(getNextId(), weight, value, holder);
         if(!isValidCapacity(capacity)) capacity = getDefaultCapacity();
         this.capacity = capacity;

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
     */
    private final Map<Long, ArrayList<Equipment>> contents = new HashMap<>();


    /**
     * @return  The contents of this backpack
     */
    public Map<Long, ArrayList<Equipment>> getContents() {
        return contents;
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
        for (Map.Entry<Long, ArrayList<Equipment>> entry: getContents().entrySet()) {
            for (Equipment eq: entry.getValue()) {
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
        for (Map.Entry<Long, ArrayList<Equipment>> entry: getContents().entrySet()) {
            for (Equipment eq: entry.getValue()) {
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
    public static boolean isValidValue(int value) {
        return (0 <= value) && (value <= getMaxValue());
    }

    /**
     * @return  The maximum value a backpack can have.
     */
    public static int getMaxValue() {
        return 500;
    }

    /*
        Equipment Holder implementation
     */

    /**
     * Checks if the given equipment can be added to this backpack
     *
     * @param   equipment
     *          The equipment to check
     * @return  True if and only if the given equipment is not broken and this backpack can pick up the weight of the
     *          given equipment, otherwise return false.
     *          | result ==
     *          | ( !equipment.isBroken() ) &&
     *          | ( canPickup(equipment.getWeight() )
     */
    @Override
    public boolean canPickup(Equipment equipment) {
        return canPickup(equipment.getWeight()) && !equipment.isBroken();
    }

    /**
     * Checks if the given weight can be added to this backpack
     *
     * @param   weight
     *          The given weight
     * @return  True if and only if the weight added to the current load doesn't exceed the capacity of this backpack.
     *          Otherwise, return false.
     *          | result == getLoad() + weight <= getCapacity
     */
    @Override
    public boolean canPickup(double weight) {
        return getLoad() + weight <= getCapacity();
    }

    /**
     * Removes the given equipment from its contents and makes the holder of the given equipment not effective.
     *
     * @param   equipment
     *          The equipment to remove
     * @post    Removes the given equipment from the contents
     *          | !( new.getContents(equipment.getId()).contains(equipment) )
     * @effect  The new holder of the given equipment is not effective
     * @throws  IllegalArgumentException
     *          If the given equipment is not directly located inside the contents of this backpack
     *          | !( getContents().containsKey(equipment.getId()) ) ||
     *          | !( getContents().get(equipment.getId()).contains(equipment) )
     */
    @Override
    public void drop(Equipment equipment)
            throws IllegalArgumentException, BrokenEquipmentException, InvalidHolderException {

        if( !(getContents().containsKey(equipment.getId())) || !(getContents().get(equipment.getId()).contains(equipment)))
            throw new IllegalArgumentException("Equipment is not located inside the contents of this backpack");

        getContents().get(equipment.getId()).remove(equipment);
        equipment.setHolder(null);
    }

    /**
     * Picks up an equipment by adding it to its contents and changing the holder of the equipment to this
     *
     * @param   equipment
     *          The equipment to pick up
     * @post    The equipment is added to the contents of this backpack
     *          | new.getContents().get(equipment.getId()).contains(equipment)
     * @effect  The holder of the given equipment is set to this backpack
     *          | (new equipment).getHolder() == this
     * @effect  If the given equipment has an effective holder, then said holder drops this item
     *          | if (equipment.getHolder() != null)
     *          | then
     *          | equipment.getHolder().drop(equipment)
     * @throws  IllegalArgumentException
     *          The given equipment cannot be picked up
     *          | !canPickup(equipment)
     */
    @Override
    public void pickup(Equipment equipment)
            throws IllegalArgumentException, BrokenEquipmentException, InvalidHolderException {

        if(!canPickup(equipment)) throw new IllegalArgumentException("Cannot pickup this Equipment");

        if(equipment.getHolder() != null) equipment.getHolder().drop(equipment);
        equipment.setHolder(this);
        if(!getContents().containsKey(equipment.getId())) getContents().put(equipment.getId(), new ArrayList<>());
        getContents().get(equipment.getId()).add(equipment);
    }

}
