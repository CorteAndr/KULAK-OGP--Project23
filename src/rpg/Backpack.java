package rpg;

import be.kuleuven.cs.som.annotate.Basic;
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

         super(getNextId(), weight, value, holder, capacity);

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
     * @return  True if and only if
     */
    @Override
    public boolean canPickup(Equipment equipment) {
        return canPickup(equipment.getWeight()) && !equipment.isBroken();
    }

    @Override
    public boolean canPickup(double weight) {
        return getLoad() + weight <= getCapacity();
    }

    @Override
    public void drop(Equipment equipment) {

    }

    @Override
    public void pickup(Equipment equipment) throws IllegalArgumentException {
        if(!canPickup(equipment)) throw new IllegalArgumentException("Cannot pickup this Equipment");
    }

}
