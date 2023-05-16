package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;

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
     * Initializes this weapon with the given weight and damage and a generated identification and calculated value
     *
     * @param   weight
     *          The weight for the new weapon
     * @param   damage
     *          The damage for the new weapon
     *
     * @effect  Initializes this weapon with the given weight and damage, generated identification and value gained from
     *          value
     */
    public Weapon(double weight, int damage) {
        super(getNextId(), weight, getValueFromDamage(damage));
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
     * Variable referencing the maximum damage
     */
    private static final int maxDamage = 100;

    /**
     * Sets this weapon's damage to the given damage
     *
     * @param   damage
     *          The given damage
     *
     * @pre     The given damage for this weapon should be valid
     *          | isValidDamage(damage)
     *
     * @post    Sets the damage of this weapon to the given damage
     *          | new.getDamage() == damage
     * @effect  The value of this weapon is updated with the value associated with the given damage
     *          | setValue(getValueFromDamage(damage))
     */
    @Model @Raw
    private void setDamage(int damage) throws  IllegalArgumentException {
        assert isValidDamage(damage): damage + " is an invalid damage"; // Dynamic Verification
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
     *
     * @effect  If the given amount is not lesser than the current damage then this weapon is discarded
     *          | if (amount >= getDamage())
     *          | then this.discard()
     * @effect  If the given amount is lesser than the current damage then the damage of this weapon
     *          is set to the old damage decreased with the given amount
     *          | if (amount < getDamage()
     *          | then setDamage(getDamage()-amount)
     * @throws  BrokenItemException
     *          This weapon is broken
     *          | isBroken()
     */
    @Override
    @Raw
    public void degrade(int amount) throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        if(amount >= getDamage()) {
            discard();
        } else {
            setDamage(getDamage() - amount);
        }
    }

    /**
     * Repairs this weapon with the given amount
     *
     * @param   amount
     *          The amount that should be repaired
     *
     * @pre     The given amount should be a positive integer
     *          | amount > 0
     *
     * @effect  Sets the damage to the old damage increased with the given amount
     *          | setDamage(getDamage() + amount)
     * @throws  BrokenItemException
     *          This weapon is broken
     *          | isBroken()
     */
    @Override
    public void repair(int amount) throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        setDamage(getDamage() + amount);
    }

    /**
     * @return  The damage attribute of this weapon.
     */
    @Basic
    public int getDamage() {
        return damage;
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
        return ((1 <= damage) && (damage <= maxDamage) && (damage % 7 == 0));
    }

    /**
     * Variable referencing the value associated with 1 damage
     */
    private static final int valueDamageFactor = 2;

    /**
     * Returns the value associated with the given damage
     *
     * @param   damage
     *          The given damage
     * @return  2 times the given damage
     *          | result == (valueDamageFactor * damage)
     */
    public static int getValueFromDamage(int damage) {
        return valueDamageFactor * damage;
    }

    /**
     * @return  The shininess of this weapon as an integer that is its value added with a random integer between 10 and 50
     *          | result == getValue() + randomint(10...50)
     */
    @Override
    public int getShiny() {
        return getValue() + (new Random()).nextInt(10, 50);
    }


    @Override
    public String toString () {
        return super.toString() + String.format(", damage: %d", getDamage());
    }
}
