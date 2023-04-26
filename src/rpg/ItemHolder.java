package rpg;

import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidHolderException;

/**
 * Interface for Item Holders
 */
public interface ItemHolder {

    /**
     * Checks if the given item can be picked up
     *
     * @param   item
     *          The item to check
     *
     * @return  | ? true
     */
    boolean canPickup(Item item);

    /**
     * Checks if the given weight can be picked up
     *
     * @param   weight
     *          The weight to check
     *
     * @return  | ? true
     */
    boolean canPickup(double weight);

    /**
     * Checks if given item is located in the contents of the holder
     *
     * @param   item
     *          The given item
     *
     * @return  | ? true
     */
    boolean holdsItem(Item item);

    /**
     * Drops the item on the ground.
     *
     * @param   item
     *          The given item on the ground
     *
     * @post    The given item is located in the contents of this Item Holder
     *          | holdsItem(item)
     * @post    The given item has as Item Holder this
     *          | (new item).getHolder() == this
     *
     * @throws  IllegalArgumentException    [CAN]
     *          | ? true
     * @throws  InvalidHolderException      [CAN]
     *          | ? true
     */
    void drop(Item item) throws IllegalArgumentException, InvalidHolderException;

    /**
     * Picks up the given item
     *
     * @param   item
     *          The given item
     *
     * @throws  IllegalArgumentException    [CAN]
     *          | ? true
     * @throws  BrokenItemException         [CAN]
     *          | ? true
     * @throws  InvalidHolderException      [CAN]
     *          | ? true
     */
    void pickup(Item item) throws IllegalArgumentException, BrokenItemException, InvalidHolderException;
}
