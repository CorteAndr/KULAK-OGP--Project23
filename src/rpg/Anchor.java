package rpg;

/**
 * An enum for anchors
 *
 * @author  Corteville Andrew
 */
public enum Anchor {
    LEFT_HAND,
    RIGHT_HAND,
    BACK,
    BODY,
    BELT,
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG,
    TAIL,
    HEAD;

    /**
     * Gets the anchor that has the given name
     *
     * @param   name
     *          The name of the desired anchor
     *
     * @return  The Anchor that has the given name or null if no such Anchor exists
     *          | for each anchor: Anchor.values()
     *          |   if(anchor.name().equals(name))
     *          |   then result == anchor
     *          | result == null
     */
    public static Anchor get(String name) {
        for(Anchor anchor: Anchor.values()) {
            if(anchor.name().equals(name)) return anchor;
        }
        return null;
    }
}
