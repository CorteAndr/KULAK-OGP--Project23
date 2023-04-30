package rpg.exceptions;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import rpg.Item;
import rpg.ItemHolder;

import java.io.Serial;

/**
 * An exception class that is thrown when an item is added to an invalid holder
 *
 * @author  Corteville Andrew
 */
public class InvalidHolderException extends Exception {

    /**
     * Required since this class inherits from Exception
     */
    @Serial
    private static final long serialVersionUID = 5289524L;

    /**
     * Initializes this exception with the given holder and item
     *
     * @param   holder
     *          The given holder
     * @param   item
     *          The given item
     * @post    The holder of this exception is set to the given holder
     *          | new.getHolder() == holder
     * @post    The item of this exception is set to the given holder
     *          | new.getItem() == item
     */
    public InvalidHolderException(ItemHolder holder, Item item) {
        this.holder = holder;
        this.item = item;
    }

    /**
     * Variable referencing the invalid holder
     */
    private final ItemHolder holder;

    /**
     * Variable referencing the item that was attempted to add to the holder
     */
    private final Item item;

    /**
     * Returns the invalid holder
     */
    @Basic @Immutable
    public ItemHolder getHolder() {
        return holder;
    }

    /**
     * @return The item that was attempted to add to the holder
     */
    @Basic @Immutable
    public Item getItem() {
        return item;
    }


}
