package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidHolderException;

import java.util.Random;

/**
 * A class of Weapons
 *
 * @author  Corteville Andrew
 *
 * @invar   Each weapon has a valid damage attribute
 *          | isValidDamage(getDamage)
 */
public class Weapon extends Item implements Degradable {

    /*
        Constructors
     */
    /**
     *  Initializes this weapon with a generated identification and given weight, holder, value and damage;
     *
     * @param   weight
     *          The weight of the new weapon
     * @param   holder
     *          The holder of the new weapon
     * @param   damage
     *          The damage of the new weapon
     *
     * @effect  Initializes this weapon with a currently available identification, the given weight and holder,
     *          and value set to value evaluated from its damage
     *          | super(getNextId(), weight, getValueFromDamage(damage), holder)
     * @effect    The damage of this weapon is set to the given damage
     *          | setDamage(damage)
     */
    public Weapon(double weight, ItemHolder holder, int damage)
            throws IllegalArgumentException, InvalidHolderException, BrokenItemException {

        super(getNextId(), weight, getValueFromDamage(damage), holder);
        setDamage(damage);
    }

    /*
        Identification (TOTAL)
     */

    /**
     * Variable referencing the smallest valid identification number for a weapon
     * | isValidId(result)
     */
    private static long currentId = 0L;

    /**
     * @return A valid identification number
     * | result == getCurrentId()
     * | && isValidId(result)
     */
    @Override
    public long getValidId() {
        return getCurrentId();
    }

    /**
     * Returns the currentId and increments the currentId
     * @effect  Increments the currentId
     *          | incrementCurrentId()
     * @return  Returns the lowest unused valid identification (aka currentId)
     *          | result == (old Weapon).getCurrentId()
     */
    private static long getNextId() {
        long id = getCurrentId();
        incrementCurrentId();
        return id;
    }

    /**
     * Checks if the given is a valid identification number.
     *
     * @param   id
     *          The identification number to check
     * @return  True if and only if the given number is divisible by 3 and even.
     *          | result == (id % 6 == 0)
     */
    @Override
    public boolean canHaveAsId(long id) {
        return id % 6 == 0;
    }

    /**
     * @return  A valid unused identification number
     */
    @Basic
    public static long getCurrentId() {
        return Weapon.currentId;
    }

    /**
     * Increments the current Weapon identification with the given increment if the new identification is valid
     *
     * @post    The currentId is updated with an increment of 6
     *          | new.getCurrentId() == old.getCurrentId() + 6
     */
    @Model
    private static void incrementCurrentId() {
        Weapon.currentId += 6;
    }
    /*
        Value (DEFENSIVE)
     */

    /**
     * Checks if a given value is a valid value for a weapon
     *
     * @param   value
     *          The value to check
     * @return  True if and only the value is an integer between 1 and 200 (inclusive)
     *          | result == (
     *          | (1 <= value) &&
     *          | (value <= 200))
     */
    @Override
    public boolean canHaveAsValue(int value) {
        return ((1 <= value) && (value <= getMaxValue()));
    }

    /**
     * @return  The maximum value a weapon can have
     */
    public static int getMaxValue() {
        return 200;
    }

    /*
        Damage (NOMINAL)
     */

    /**
     * Variable referencing the damage of this weapon
     */
    private int damage;

    /**
     *   Sets this weapon's damage to the given damage
     *
     * @param   damage
     *          The given damage
     * @pre     The given damage for this weapon should be valid
     *          | isValidDamage(damage)
     * @post    Sets the damage of this weapon to the given damage
     *          | new.getDamage() == damage
     * @effect  The value of this weapon is updated with the value associated with the given damage
     *          | setValue(getValueFromDamage(damage))
     * @throws BrokenItemException
     *          This weapon is broken
     *          | isBroken()
     */
    @Model @Raw
    private void setDamage(int damage) throws BrokenItemException, IllegalArgumentException {
        assert isValidDamage(damage): damage + " is an invalid damage";
        if(isBroken()) throw new BrokenItemException(this);
        setValue(getValueFromDamage(damage));
        this.damage = damage;
    }

    /**
     * Degrades the damage of this weapon with the given amount
     *
     * @param   amount
     *          The amount of degradation
     *
     * @pre     The given amount should be positive
     *          | amount > 0
     * @pre     The given amount should be less than the current damage the weapon has
     *          | amount < getDamage()
     *
     * @effect  The damage of this weapon is set to the old damage decreased with the given amount
     *          | setDamage(getDamage()-amount)
     * @throws  BrokenItemException
     *          This weapon is broken
     *          | isBroken()
     */
    @Override
    @Raw
    public void degrade(int amount) throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        if(amount > getDamage()) destroy();

        setDamage(getDamage()-amount);
    }


    @Override
    public void repair(int amount) throws BrokenItemException {

    }

    /**
     * @return  The damage attribute of this weapon.
     */
    @Basic
    public int getDamage() {
        return damage;
    }

    /**
     * @return  The maximum damage a weapon can have.
     */
    private static int getMaxDamage() {
        return 100;
    }

    /**
     * Checks if the given damage is valid for a weapon
     *
     * @param   damage
     *          The damage to check
     * @return  True if and only if the given damage is an integer that is divisible by 7 and lies between 1 and the
     *          maximum allowed damage
     *          | result ==
     *          | (1 <= damage) &&
     *          | (damage <= getMaxDamage()) &&
     *          | (damage % 7 == 0)
     */
    public static boolean isValidDamage(int damage) {
        return ((1 <= damage) && (damage <= getMaxDamage()) && (damage % 7 == 0));
    }


    /**
     * Returns the value associated with the given damage
     *
     * @param   damage
     *          The given damage
     * @return  2 times the given damage
     *          | result == (2 * damage)
     */
    public static int getValueFromDamage(int damage) {
        return 2 * damage;
    }

    /**
     * @return  The shininess of this weapon as an integer that is its value added with a random integer between 10 and 50
     *          | result == getValue() + randomint(10, 50)
     */
    @Override
    public int getShiny() {
        return getValue() + (new Random()).nextInt(10, 50);
    }

}
