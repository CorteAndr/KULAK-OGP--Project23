package rpg;

import rpg.exceptions.BrokenItemException;

/**
 * An interface for items that can degrade over and be repaired
 *
 * @author  Corteville Andrew
 */
public interface Degradable {

    /**
     * Degrades the object with the given amount
     *
     * @param   amount
     *          The amount of degradation
     *
     * @throws  BrokenItemException
     *          | ? true
     */
    void degrade(int amount) throws BrokenItemException;

    /**
     * Repairs the object with the given amount
     *
     * @param   amount
     *          The amount that should be repaired
     *
     * @throws  BrokenItemException
     *          | ? true
     */
    void repair(int amount) throws BrokenItemException;



}
