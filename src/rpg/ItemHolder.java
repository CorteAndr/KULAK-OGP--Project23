package rpg;

import rpg.exceptions.BrokenItemException;
import rpg.exceptions.DeadEntityException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

/**
 * Interface for Item Holders
 *
 * @author  Corteville Andrew
 */
public interface ItemHolder {

    /**
     * Checks if the given item can be picked up
     *
     * @param   item
     *          The item to check
     *
     * @return  The given item can be picked up by the entity
     *          | ? true
     * @throws  IllegalArgumentException
     *          | ? true
     */
    boolean canPickup(Item item) throws IllegalArgumentException;

    /**
     * Checks if the given weight can be picked up
     *
     * @param   weight
     *          The weight to check
     *
     * @return  True if and only if the given weight can be added to the item holder's contents
     *          | ? true
     */
    boolean canPickup(double weight) throws IllegalArgumentException;

    /**
     * Checks if given item is located in the contents of the holder
     *
     * @param   item
     *          The given item
     *
     * @return  True if and only if the given item is holden by the item holder, otherwise return false
     *          | ? true
     * @throws  IllegalArgumentException
     *          | ? true
     */
    boolean holdsItemDirectly(Item item) throws IllegalArgumentException;

    /**
     * Checks if the given item is directly or indirectly held by the holder
     */
    boolean holdsItem(Item item);

    /**
     * Returns the number of items held both directly and indirectly of the given type
     *
     * @param   type
     *          The type of item to find
     * @return  The number of items held (directly or indirectly) of the given type.
     *          | ?
     */
    int getNbItemsOfTypeHeld(Class<? extends Item> type);

    /**
     * Drops the item on the ground.
     *
     * @param   item
     *          The given item on the ground
     *
     * @post    The given item is no longer located in the contents of this Item Holder
     *          | !holdsItem(item)
     * @post    The given item has as Item Holder this
     *          | (new item).getHolder() == this
     *
     * @throws  IllegalArgumentException    [CAN]
     *          | ? true
     */
    void drop(Item item) throws IllegalArgumentException;

    /**
     * Picks up the given item
     *
     * @param   item
     *          The given item
     *
     * @throws  IllegalArgumentException    [CAN]
     *          | ? true
     * @throws  InvalidHolderException      [CAN]
     *          | ? true
     * @throws  DeadEntityException         [CAN]
     *          | ? true
     */
    void pickup(Item item) throws IllegalArgumentException,
            InvalidHolderException, DeadEntityException;
}
