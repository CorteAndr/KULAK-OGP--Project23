package rpg;

public interface EquipmentHolder {
    boolean canPickup(Equipment equipment);

    boolean canPickup(double weight);
    void drop(Equipment equipment);
    void pickup(Equipment equipment) throws IllegalArgumentException ;
}
