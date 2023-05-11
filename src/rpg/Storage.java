package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.Random;

/**
 * An abstract class of Storages
 *
 * @author  Corteville Andrew
 *
 * @invar   The total weight of items located in a storage should not exceed the capacity of the storage.
 */
public abstract class Storage extends Item {

    /**
     * Initializes this storage with the given id, weight and value
     *
     * @param   id
     *          The identification of the new storage
     * @param   weight
     *          The weight of the new storage
     * @param   value
     *          The value of the new storage
     *
     * @effect  Initializes this storage with the given identification, weight and value
     *          | super(id, weight, value)
     */
    @Raw
    protected Storage(long id, double weight, int value) {
        super(id, weight, value);
    }

    /**
     *
     * @param id
     * @param weight
     */
    @Raw
    public Storage(long id, double weight) {
        super(id, weight, 0);
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
