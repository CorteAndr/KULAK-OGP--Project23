package rpg.exceptions;

import be.kuleuven.cs.som.annotate.Raw;
import rpg.Anchor;
import rpg.Entity;
import rpg.Item;

import java.io.Serial;

/**
 * An exception that is thrown when an attempt is made to add an item to an invalid anchor of an entity
 */
public class InvalidAnchorException extends Exception {

    /**
     * Required since this class inherits from Exception
     */
    @Serial
    private static final long serialVersionUID = 244L;

    /**
     * Initializes this exception with the given entity, item and name of the anchor where the exception occurred.
     *
     * @param   entity
     *          The entity to whom the item was attempted to add
     * @param   item
     *          The item that was attempted to add
     * @param   anchorName
     *          The name of the name where the item was attempted to add
     *
     * @post    The entity this exception references is set to the given entity.
     *          | new.getEntity() == entity
     * @post    The item this exception references is set to the given item.
     *          | new.getItem() == item
     * @post    The anchor name of this exception is set to the given anchor name.
     *          | new.getAnchorName() == anchorName
     */
    @Raw
    public InvalidAnchorException(Entity entity, Item item, Anchor anchorName) {
        this.entity = entity;
        this.item = item;
        this.anchorName = anchorName;
    }

    /**
     * Variable referencing the entity to which the item was attempted to add.
     */
    private final Entity entity;

    /**
     * Variable referencing the item that was attempted to add.
     */
    private final Item item;

    /**
     * Variable referencing the name of the anchor to which the item was attempted to add.
     */
    private final Anchor anchorName;

    /**
     * Returns the entity referenced by this exception.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Returns the item referenced by this exception.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Returns the name of the anchor from the exception.
     */
    public Anchor getAnchorName() {
        return anchorName;
    }
}
