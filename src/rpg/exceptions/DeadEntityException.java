package rpg.exceptions;

import be.kuleuven.cs.som.annotate.Basic;
import rpg.Entity;

import java.io.Serial;

/**
 * An exception class that is thrown when on dead entity is attempted to be modified
 *
 * @invar   Each exception must have a dead entity
 *          | getEntity().isDead()
 */
public class DeadEntityException extends Exception {

    /**
     * Required since this class inherits from Exception
     */
    @Serial
    private static final long serialVersionUID = 536L;

    /**
     * Initializes this exception with the given entity
     *
     * @param   entity
     *          The given entity
     *
     * @post    The
     */
    public DeadEntityException(Entity entity) {
        if(!entity.isDead()) throw new IllegalArgumentException("The given entity is not dead");
        this.entity = entity;
    }

    /**
     * Variable referencing the dead entity at which the error occurred.
     */
    private final Entity entity;

    /**
     * Return the entity at which this exception occurred.
     */
    @Basic
    public Entity getEntity() {
        return entity;
    }

}
