package rpg.exceptions;

import rpg.Item;

/**
 * An exception class that is thrown when a piece of broken item is attempted to be modified.
 *
 * @invar   Each exception has effective piece of item.
 *          | getItem() != null
 * @invar   Each exception has a piece of broken item.
 *          | getItem().isBroken()
 */
public class BrokenItemException extends Exception {

    /**
     * Initializes this exception with the given broken item.
     *
     * @param   item
     *          The broken item
     * @post    The item of this exception is set to the given item.
     *          | new.getItem() == item
     * @throws  IllegalArgumentException
     *          If the given item is not effective
     *          | item == null
     * @throws  IllegalArgumentException
     *          If the given item is not broken
     *          | !item.isBroken()
     */
    public BrokenItemException(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (!item.isBroken()) throw new IllegalArgumentException(item + " is not broken");
        this.item = item;
    }

    /**
     * Variable referencing the broken item.
     */
    private final Item item;

    /**
     * Returns the broken item at which the exception occurred.
     */
    public Item getItem() {
        return item;
    }

}
