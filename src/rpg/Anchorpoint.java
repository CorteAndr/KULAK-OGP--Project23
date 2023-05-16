package rpg;

/**
 * An enum for anchor-points
 *
 * @author  Corteville Andrew
 *
 * @invar   Each anchorpoint must have a valid name
 *          | isValidName(getName())
 */
public enum Anchorpoint {
    LEFT_HAND("Left hand"),
    RIGHT_HAND("Right hand"),
    BACK("Back"),
    BODY("Body"),
    BELT("Belt"),
    LEFT_ARM("Left arm"),
    RIGHT_ARM("Right arm"),
    LEFT_LEG("Left leg"),
    RIGHT_LEG("Right leg"),
    TAIL("Tail"),
    HEAD("Head");

    /**
     * Initializes the anchorpoint with the given name
     *
     * @param   name
     *          The name of the anchorpoint
     *
     * @post    The name of the anchorpoint is set to the given name
     *          | new.getName() == name
     * @throws  IllegalArgumentException
     *          The given name is not valid
     *          | !Anchorpoint.isValidName(name)
     */
    Anchorpoint(String name) {
        if(!isValidName(name)) throw new IllegalArgumentException("The given name is not valid");
        this.name = name;
    }

    /**
     * Variable referencing the name of the anchor point
     */
    private String name = null;

    /**
     *  @return  The name of this anchor point
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the given name is a valid name for an anchorpoint
     *
     * @param   name
     *          The name to check
     * @return  True if and only if the given name is effective and only exist out of letters and spaces, is of length
     *          at least 1 and the first character is an uppercase letter
     *          | result == (
     *          |   (name != null) && (name.matches("[A-Z][a-zA-Z ]*"]))
     *          | )
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("[A-Z][a-zA-Z ]*");
    }

    /**
     * Gets the anchor that has the given name
     *
     * @param   name
     *          The name of the desired anchor
     *
     * @return  The Anchorpoint that has the given name or null if no such Anchorpoint exists
     *          | for each anchor: Anchorpoint.values()
     *          |   if(anchor.getName().equals(name))
     *          |   then result == anchor
     *          | result == null
     */
    public static Anchorpoint get(String name) {
        for(Anchorpoint anchor: Anchorpoint.values()) {
            if(anchor.getName().equals(name)) return anchor;
        }
        return null;
    }

    /**
     * Checks if the given item can be held at this anchor
     *
     * @param   item
     *          The item to check
     * @return  True if and only if the given item is not effective or is either a Purse and this is a Belt or
     *          is not a Purse and this is not a Belt and the given item is not broken
     *          | result == (
     *          |   item == null ||
     *          |   (item instanceof Purse && this == BELT) ||
     *          |   ( !(item instanceof Purse) && !item.isBroken())
     *          | )
     */
    public boolean canHoldItem(Item item) {
        return (item == null ||
                (item instanceof Purse && this == BELT) ||
                ( (!(item instanceof Purse) && this != BELT) && !item.isBroken()));
    }
}
