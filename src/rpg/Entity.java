package rpg;

import java.util.HashMap;

public abstract class Entity implements EquipmentHolder {

    /*
        Constructors
     */

    /*
        Destructor
     */

    /**
     * Variable referencing the state of this entity. i.e. true means this entity is dead and thus not effective.
     */
    private boolean isDead;


    /*
        Name (DEFENSIVE)
     */
    /**
     * Variable referencing the name of the entity.
     */
    private String name;

    /**
     * If the given name is valid, sets to name to the given name.
     *
     * @param   name
     *          The given name
     * @post    If the given name is valid, the name of this entity is set to the given name
     *          | if (isValidName(name))
     *          | then new.getName() == name
     * @throws  InvalidNameException
     *          If the given name is not valid.
     *          | !isValidName(name)
     */
    public void setName(String name) throws IllegalArgumentException {
        if(isValidName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(name + " is not a valid name for a " + getClass().toString());
        }
    }


    public abstract boolean isValidName(String name);

    /*
        Hitpoints
     */

    /**
     *  Variable referencing the maximum amount of hitpoints this entity can have
     */
    private int maxHitpoints;

    /**
     * Variable referencing the current amount of hitpoints of the entity.
     */
    private int hitpoints;

    /*
        Anchors
     */

    /**
     * Map of all anchors of this entity of its name and which item it holds.
     */
    private HashMap<String, Equipment> anchors;

    /**
     *
     */
    private void setAnchors(HashMap<String, Equipment> anchors) {
        this.anchors = anchors;
    }

    public Equipment getAnchor(String anchorName) throws IllegalArgumentException {
        if(anchors.containsKey(anchorName))
        {
                if (anchors.get(anchorName) != null) {
                    return anchors.get(anchorName);
                }
                return null;
        }
        throw new IllegalArgumentException(anchorName + " does not exist on " + this);
    }

}
