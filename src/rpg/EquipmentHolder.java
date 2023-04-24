package rpg;

import rpg.exceptions.BrokenEquipmentException;
import rpg.exceptions.InvalidHolderException;

public interface EquipmentHolder {
    boolean canPickup(Equipment equipment);

    boolean canPickup(double weight);
    void drop(Equipment equipment) throws BrokenEquipmentException, InvalidHolderException;
    void pickup(Equipment equipment) throws IllegalArgumentException, BrokenEquipmentException, InvalidHolderException;
}
