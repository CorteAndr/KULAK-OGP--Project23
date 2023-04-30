package rpg;

import rpg.exceptions.InvalidAnchorException;

//TODO Everything
public class Monster extends Entity {
    public Monster() throws InvalidAnchorException {
        super("Monster", 5000);
    }

    @Override
    public double getCapacity() {
        return 0;
    }
}
