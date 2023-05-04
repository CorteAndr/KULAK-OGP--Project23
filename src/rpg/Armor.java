package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidHolderException;

import java.util.*;

/**
 * A class of Armors
 *
 * @author  Corteville Andrew
 *
 * @invar   Each armor has a valid maximum protection
 *          | isValidMaxProtection(getMaxProtection())
 * @invar   Each armor has a valid effective protection
 *          | canHaveAsEffectiveProtection(getEffectiveProtection())
 * @invar   Each armorType has a valid name
 *          | isValidNameForArmorType(name)
 */
public class Armor extends Item implements Degradable {

    /**
     * Initializes this Armor with the given id, weight, value,  holder and maximum protection.
     *
     * @param   id
     *          The given identification
     * @param   weight
     *          The given weight
     * @param   value
     *          The given value
     * @param   holder
     *          The given holder
     * @param   maxProtection
     *          The given maximum protection
     *
     * @effect  This armor is initialized with the given id, weight, value and holder.
     *          | super(id, weight, value, holder)
     * @post    The identification of this Armor is added to the used ids
     *          | new.usedIds.contains(getId())
     * @post    The maximum protection is set to the given protection, if valid otherwise it is set to
     *          the default maximum protection.
     *          | if(isValidMaxProtection(maxProtection))
     *          | then
     *          | new.getMaxProtection() == maxProtection
     *          | else
     *          | new.getMaxProtection() == getDefaultMaxProtection()
     * @effect  The effective protection of this armor is set the maximum protection of this Armor;
     *          | setEffectiveProtection(getMaxProtection())
     */
    @Raw
    public Armor(long id, double weight, int value, ItemHolder holder, int maxProtection)
            throws InvalidHolderException {

        super(isValidNewId(id) ? id: getNextId(), weight, value, holder);
        usedIds.add(getId());

        if (!isValidMaxProtection(maxProtection)) maxProtection = getDefaultMaxProtection();
        this.maxProtection = maxProtection;
        setEffectiveProtection(getMaxProtection());
    }

    /*
        Identification
     */

    /**
     * Class variable that contains all used identifications
     *
     * @invar   usedIds references an effective set
     *          | usedIds != null
     * @invar   Each element of usedIds must be effective
     *          | for each id in usedIds:
     *          |   id != null
     * @invar   Each element of usedIds must be a valid id for an Armor
     *          | for each id in usedIds:
     *          |   canHaveAsId(id)
     */
    private static final Set<Long> usedIds = new HashSet<>();

    /**
     * @return  A valid identification number that isn't currently used
     *          | isValidId(result) && !usedIds.contains(result)
     */
    @Override
    public long getValidId() {
        return getNextId();
    }

    /**
     * Checks if an id is valid new id for an armor.
     *
     * @param   id
     *          The identification to check
     * @return  True if and only if the given id is a prime number
     *          | result == isPrime(id) && !usedIds.contains(id)
     */
    protected static boolean isValidNewId(long id) {
        return isPrime(id) && !usedIds.contains(id);
    }

    /**
     * Checks if the given id is a valid id for this armor
     *
     * @param   id
     *          The identification number to check.
     * @return  True if and only if the given id is prime.
     *          | isPrime(id)
     */
    @Override
    public boolean canHaveAsId(long id) {
        return isPrime(id);
    }

    /**
     * Checks if the given number is prime.
     *
     * @param   number
     *          The number to check
     * @return  True if and only if the given number is prime.
     *          |
     */
    private static boolean isPrime(long number) {
        if (number <= 1) return false;
        for (int i=2; i <= Math.sqrt(number); i++) if (number % i == 0) return false;
        return true;
    }

    /**
     * @return  A valid identification number that isn't currently used
     *          | isValidNewId(result)
     */
    private static long getNextId() {
        if(isValidNewId(2L)) return 2L;
        long currentId = 3;
        while(!isValidNewId(currentId)) currentId += 2;
        return currentId;
    }

    /*
        Value (DEFENSIVE)
     */

    /**
     * The maximum value of this armor can have
     * @return  The maximum value of this armor
     *          | super.getValue()
     */
    public int getMaxValue() {
        return super.getValue();
    }

    /**
     * @return  The effective value of this armor meaning its maximum value multiplied with the
     *          effectiveness of this armor (effectiveProtection/maxProtection)
     *          | result == getMaxValue() * getEffectiveProtection()/getMaxProtection()
     */
    @Override
    public int getValue() {
        return getMaxValue() * getEffectiveProtection()/getMaxProtection();
    }

    /*
        Protection
     */
    /**
     * Variable referencing the maximum protection this armor can have.
     */
    private final int maxProtection;

    /**
     * @return  The maximum protection this armor can offer
     */
    @Basic @Immutable
    public int getMaxProtection() {
        return maxProtection;
    }

    /**
     * Checks if the given maximum protection is valid
     *
     * @param   maxProtection
     *          The given maximum protection
     * @return  True if and only if the maximum protection is an integer between 1 and 100 (inclusive)
     *          | result ==
     *          |   ( 1 <= maxProtection) &&
     *          |   ( maxProtection <= 100)
     */
    public static boolean isValidMaxProtection(int maxProtection) {
        return (1 <= maxProtection) && (maxProtection <= 100);
    }

    /**
     * @return  The default maximum protection that is used when the given maximum protection is invalid.
     */
    @Immutable
    private static int getDefaultMaxProtection() {
        return 25;
    }


    /**
     * Variable referencing the actual protection this armor offers
     */
    private int effectiveProtection;

    /**
     * @return  The effective protection this armor offers
     */
    @Basic
    public int getEffectiveProtection() {
        return effectiveProtection;
    }

    /**
     * Sets the effective protection of this armor to the given protection.
     *
     * @param   effectiveProtection
     *          The given protection
     * @post    The effective protection of this armor is set to the given protection
     *          | new.getEffectiveProtection() == effectiveProtection
     * @throws  IllegalArgumentException
     *          The given protection is an invalid protection for this Armor
     *          | !isValidEffectiveProtection(effectiveProtection)
     */
    @Model
    @Raw
    private void setEffectiveProtection(int effectiveProtection) throws IllegalArgumentException {
        if(!canHaveAsEffectiveProtection(effectiveProtection)) throw new IllegalArgumentException("Invalid effective protection");
        this.effectiveProtection = effectiveProtection;
    }

    /**
     * Degrades the effective protection of this armor by the given amount
     *
     * @param   amount
     *          The given amount
     *
     * @pre     Amount should be a strictly positive integer
     *          | 0 < amount
     * @pre     Amount should be less than the effectiveProtection of this Armor
     *          | amount <= getEffectiveProtection()
     *
     * @effect  Sets the effectiveProtection of this Armor the old effective protection reduced by the given amount
     *          | setEffectiveProtection(getEffectiveProtection()-amount)
     * @throws  BrokenItemException
     *          This armor is broken
     *          | isBroken()
     */
    @Raw
    public void degrade(int amount) throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        if(amount > 0 && amount <= getEffectiveProtection())
            setEffectiveProtection(getEffectiveProtection()-amount);
    }

    /**
     * Repairs the effective protection of this armor by the given amount
     *
     * @param   amount
     *          The given amount
     * @pre     The given amount should be a strictly positive integer
     *          | 0 < amount
     * @pre     The given amount should be less than or equal to the difference between
     *          the maximum protection and effective protection
     *          | amount <= getMaxProtection() - getEffectiveProtection()
     *
     * @effect  Sets the effective protection of this Armor to the old effective protection increased by the given amount
     *          | setEffectiveProtection(getEffectiveProtection() + amount)
     * @throws  BrokenItemException
     *          This armor is broken
     *          | isBroken()
     */
    public void repair(int amount) throws BrokenItemException {
        if(isBroken()) throw new BrokenItemException(this);
        if( (0<=amount) && (amount<=(getMaxProtection()-getEffectiveProtection())))
            setEffectiveProtection(getEffectiveProtection() + amount);
    }


    /**
     * Checks if the given protection is a valid effective protection for this armor
     *
     * @param   effectiveProtection
     *          The protection value to check
     * @return  True if and only if the given protection lies between 1 and the maximum protection of this armor
     *          | result ==
     *          |   ( 1 <= effectiveProtection ) &&
     *          |   ( effectiveProtection <= getMaxProtection() )
     */
    @Raw
    public boolean canHaveAsEffectiveProtection(int effectiveProtection) {
        if(isBroken()) return effectiveProtection == 0;
        return (1<= effectiveProtection && effectiveProtection <= getMaxProtection());
    }

    /**
     * Map of Armor Types with their associated maximum protection
     */
    private static final Map<String, Integer> armorTypes = new HashMap<>();

    public static Map<String, Integer> getArmorTypes() {
        return armorTypes;
    }

    /**
     * Adds an armor type and its associated max protection to the list of armor types
     *
     * @param   name
     *          Name for the new armorType
     * @param   maxProtection
     *          Maximum protection associated with the armor type
     * @post    The given armor type is added to the map of armor types.
     *          | new.getArmorTypes().contains(name) &&
     *          | new.getArmorTypes().contain
     * @throws  IllegalArgumentException
     *          The given armor type name is invalid
     *          | !isValidArmorTypeName(name)
     * @throws  IllegalArgumentException
     *          The given maximum protection is invalid
     *          | !isValidMaxProtection(maxProtection)
     */
    public static void addArmorType(String name, int maxProtection) throws IllegalArgumentException {
        if(!isValidArmorTypeName(name)) throw new IllegalArgumentException("Not a valid name for an armor type");
        if(!isValidMaxProtection(maxProtection)) throw new IllegalArgumentException("Not a valid maximum protection");
        getArmorTypes().put(name, maxProtection);
    }

    /**
     * Checks if the given name is a valid Armor type name
     *
     * @param   name
     *          The name to check
     * @return  True if and only if the given name is effective and not an empty String, otherwise return false.
     *          | result ==
     *          |   (name != null) &&
     *          |   (!name.equals(""))
     */
    public static boolean isValidArmorTypeName(String name) {
        return name != null && !name.equals("");
    }

    /*
        Shiny
     */

    /**
     * @return  The shininess of this armor as its value multiplied by 3/4
     *          | result == getValue() * 3/4
     */
    @Override
    public int getShiny() {
        return getValue() * 3/4;
    }
}
