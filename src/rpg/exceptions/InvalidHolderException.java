package rpg.exceptions;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import rpg.EquipmentHolder;
import rpg.Equipment;

/**
 * An exception class that is thrown when an equipment is added to an invalid holder
 *
 * @author  Corteville Andrew
 */
public class InvalidHolderException extends Exception {

    /**
     * Initializes this exception with the given holder and equipment
     *
     * @param   holder
     *          The given holder
     * @param   equipment
     *          The given equipment
     * @post    The holder of this exception is set to the given holder
     *          | new.getHolder() == holder
     * @post    The equipment of this exception is set to the given holder
     *          | new.getItem() == equipment
     */
    public InvalidHolderException(EquipmentHolder holder, Equipment equipment) {
        this.holder = holder;
        this.equipment = equipment;
    }

    /**
     * Variable referencing the invalid holder
     */
    private final EquipmentHolder holder;

    /**
     * Variable referencing the equipment that was attempted to add to the holder
     */
    private final Equipment equipment;

    /**
     * Returns the invalid holder
     */
    @Basic @Immutable
    public EquipmentHolder getHolder() {
        return holder;
    }

    /**
     * @return The equipment that was attempted to add to the holder
     */
    @Basic @Immutable
    public Equipment getItem() {
        return equipment;
    }


}
