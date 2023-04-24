package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import rpg.exceptions.BrokenEquipmentException;
import rpg.exceptions.InvalidHolderException;

import java.util.Random;

/**
 * A class of Storages
 *
 * @author  Corteville Andrew
 *
 * @invar   The total weight of items located in a storage should not exceed the capacity of the storage.
 */
public abstract class Storage extends Equipment {

    /**
     * Initializes this Storage with the given id, weight, value, holder and capacity
     * @param   id
     *          The given id
     * @param   weight
     *          The given weight
     * @param   value
     *          The given value
     * @param   holder
     *          The given holder
     * @param   capacity
     *          The given capacity
     * @effect  Initializes this Storage with the given id, weight, value and holder.
     *          | super(id, weight, value, holder)
     * @post    The capacity is set to the given capacity if valid, otherwise it is set to a default capacity.
     *          | if(isValidCapacity(capacity))
     *          | then
     *          | new.getCapacity() == capacity
     *          | else
     *          | new.getCapacity() == getDefaultCapacity()
     */
    public Storage(long id, double weight, int value, EquipmentHolder holder)
            throws BrokenEquipmentException, InvalidHolderException {
        super(id, weight, value, holder);
    }

    /*
        Weight (TOTAL/NOMINAL?)
     */

    /**
     * @return  the weight intrinsic of this storage itself.
     */
    @Basic @Immutable
    public double getOwnWeight() {
        return super.getWeight();
    }

    /**
     * @return  The total weight of the storage
     */
    @Override
    public double getWeight() {
        return getOwnWeight() + getLoad();
    }

    /**
     * @return  The load of the contents of this storage
     */
    @Basic
    public abstract double getLoad();

    /*
        Shiny
     */

    /**
     * @return  The shininess of this storage as a random integer between -10 and 10
     *          | result == randomint(-10...10)
     */
    @Override
    public int getShiny() {
        return (new Random()).nextInt(-10, 10);
    }


}
