package rpg.exceptions;

import rpg.Equipment;

/**
 * An exception class that is thrown when a piece of broken equipment is attempted to be modified.
 *
 * @invar   Each exception has effective piece of equipment.
 *          | getItem() != null
 * @invar   Each exception has a piece of broken equipment.
 *          | getItem().isBroken()
 */
public class BrokenEquipmentException extends Exception {

    /**
     * Initializes this exception with the given broken equipment.
     *
     * @param   equipment
     *          The broken equipment
     * @post    The equipment of this exception is set to the given equipment.
     *          | new.getItem() == equipment
     * @throws  IllegalArgumentException
     *          If the given equipment is not effective
     *          | equipment == null
     * @throws  IllegalArgumentException
     *          If the given equipment is not broken
     *          | !equipment.isBroken()
     */
    public BrokenEquipmentException(Equipment equipment) {
        if (equipment == null) throw new IllegalArgumentException();
        if (!equipment.isBroken()) throw new IllegalArgumentException(equipment + " is not broken");
        this.equipment = equipment;
    }

    /**
     * Variable referencing the broken equipment.
     */
    private final Equipment equipment;

    /**
     * Returns the broken equipment at which the exception occurred.
     */
    public Equipment getItem() {
        return equipment;
    }

}
