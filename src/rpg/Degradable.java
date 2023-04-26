package rpg;

import rpg.exceptions.BrokenItemException;

/**
 * An interface for items that can degrade and repair over time
 */
public interface Degradable {

    /**
     * Degrades the object with the given amount
     *
     * @param   amount
     *          The amount of degradation
     */
    void degrade(int amount) throws BrokenItemException;

    /**
     * Repairs the object with the given amount
     * @param   amount
     *          The amount that should be repaired
     */
    void repair(int amount) throws BrokenItemException;



}
