package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.DeadEntityException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Class of Heroes
 *
 * @author  Corteville Andrew
 *
 * @invar   Each hero must have a valid intrinsic strength
 *          | isValidStrength(getStrength())
 */
public class Hero extends Entity {

    /**
     * Initializes this Hero with the given name, strength, maxHP set to 100 and default anchors of a hero
     *
     * @param   name
     *          The name for the new hero
     * @param   strength
     *          The strength of the new hero
     *
     * @effect  Initializes this new hero with the given name and strength and with the default hero anchors and
     *          maximum hit points set to 100 and actual hit points equal to the nearest lower prime of 100.
     *          | super(name, 100, getFirstLowerPrime(100))
     */
    @Raw
    public Hero(String name, double strength)
            throws IllegalArgumentException, BrokenItemException, InvalidAnchorException, InvalidHolderException {
        super(name, 100, getFirstLowerPrime(100), new HashMap<Anchorpoint, Item>(defaultAnchors), -1);
        setStrength(strength);
    }

    /**
     * Initializes this hero with the given name, maximum hit points, actual hit points, anchors and strength
     *
     * @param   name
     *          The name for the new hero
     * @param   maxHitPoints
     *          The maximum hit points for the new hero
     * @param   hitPoints
     *          The actual hit points of the new hero.
     * @param   anchors
     *          The anchors for the new hero
     * @param   strength
     *          The strength for the new hero
     *
     * @effect  Initializes this hero with the given name, maximum hit points
     *          | super(name, maxHitPoints, hitPoint)
     * @effect  The strength of this hero is set to the given strength
     *          | setStrength(strength)
     * @effect  The anchors of this hero are set to the initialized version of the given anchors
     *          | setAnchors(getInitializedAnchors(anchors))
     */
    @Raw
    public Hero(String name, int maxHitPoints, int hitPoints, Map<Anchorpoint, Item> anchors, double strength)
            throws IllegalArgumentException {
        super(name, maxHitPoints, hitPoints);
        setStrength(strength);
        try {
            setAnchors(getInitializedAnchors(anchors));
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
    }


    /*
        Strength (TOTAL)
     */

    private static final int strengthPrecision = 2;

    /**
     * Variable referencing the intrinsic strength of this hero
     */
    private double strength;

    /**
     * Return the intrinsic strength of this hero
     */
    public double getStrength() {
        return strength;
    }

    /**
     * Sets the intrinsic strength of the hero to the given strength with rounded to the precision of strength if the given
     * strength is valid, otherwise the strength is set to the maximum
     */
    @Model
    @Raw
    protected void setStrength(double strength) {
        if(canHaveAsStrength(strength)) {
           this.strength = BigDecimal.valueOf(strength).setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue();
        } else {
            this.strength = BigDecimal.valueOf(Math.max(getDefaultStrength(), getLoad()/capacityStrengthFactor))
                    .setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue();
        }
    }

    /**
     * Multiplies the intrinsic strength of this Hero with the given factor.
     *
     * @param   factor
     *          The factor with which to multiply the strength of this Hero.
     *
     * @effect  Sets the strength of this Hero to the old strength multiplied by the given factor and rounded to
     *          2 decimal places, if the given factor is strictly positive.
     *          | setStrength(BigDecimal.valueOf(getStrength()).multiply(BigDecimal.valueOf(factor), RoundingMode.HALF_UP)
     *                     .setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue())
     */
    public void multiplyStrength(int factor) {
        if(factor > 0) {
            setStrength(BigDecimal.valueOf(getStrength()).multiply(BigDecimal.valueOf(factor))
                    .setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue());
        }
    }

    /**
     * Divides of the intrinsic strength of this Hero with the given factor
     *
     * @param   factor
     *          The factor with which to divide the strength of this Hero
     *
     * @effect  Sets the strength of this Hero to the old strength divided by the given factor and rounded to 2
     *          decimal places, if the given factor is strictly positive.
     *          | if (factor > 0)
     *          | setStrength(BigDecimal.valueOf(getStrength()).divide(BigDecimal.valueOf(factor), RoundingMode.HALF_UP)
     *                     .setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue())
     */
    public void divideStrength(int factor) {
        if(factor > 0) {
            setStrength(BigDecimal.valueOf(getStrength()).divide(BigDecimal.valueOf(factor), RoundingMode.HALF_UP)
                    .setScale(strengthPrecision, RoundingMode.HALF_UP).doubleValue());
        }
    }

    /**
     * Checks if the given strength is a valid strength for this hero
     *
     * @param   strength
     *          The strength to check
     *
     * @return  True if and only if the given strength is a strictly positive double and the load of the hero does not
     *          exceed the capacity associated with the given strength, otherwise return false.
     *          | result == (
     *          |   (strength > 0) &&
     *          |   (getLoad() < getCapacityFromStrength(strength))
     *          | )
     */
    public boolean canHaveAsStrength(double strength) {
        return (strength > 0 ) && getLoad() < getCapacityFromStrength(strength);
    }

    /**
     * Returns the default strength for when the given strength during initialization is not valid.
     * @return  A valid strength for a Hero
     *          | isValidStrength(result)
     */
    private static double getDefaultStrength() {
        return 15.00;
    }


    /**
     * Returns the capacity of this hero
     *
     * @return  The capacity this hero can carry
     *          | result == getCapacityFromStrength(getStrength())
     */
    @Override
    public double getCapacity() {
        return getCapacityFromStrength(getStrength());
    }

    private static final int capacityStrengthFactor = 20;

    /**
     * Returns the capacity a Hero can carry, calculated by using strength
     *
     * @param   strength
     *          The given strength
     * @return  The capacity calculated from the strength
     *          | result == (20 * strength)
     */
    private static double getCapacityFromStrength(double strength) {
        return capacityStrengthFactor*strength;
    }

    /**
     * Variable referencing the default anchors of a Hero
     */
    private static final HashMap<Anchorpoint, Item> defaultAnchors = new HashMap<>() {{
        put(Anchorpoint.LEFT_HAND, null);
        put(Anchorpoint.RIGHT_HAND, null);
        put(Anchorpoint.BODY, null);
        put(Anchorpoint.BACK, null);
        put(Anchorpoint.BELT, null);
    }};

    //TODO documentation
    /**
     * Returns the anchors initialized for a Hero using the given anchors
     *
     * @param   anchors
     *          The anchors to base the resulting anchors on
     *
     * @pre
     *
     * @return  A map of anchors to items that has the default anchors of a hero and if the given anchors contains
     *          such that
     */
    @Raw
    private static Map<Anchorpoint, Item> getInitializedAnchors(Map<Anchorpoint, Item> anchors) {
        Map<Anchorpoint, Item> result = new HashMap<>(defaultAnchors);
        for(Map.Entry<Anchorpoint, Item> entry: result.entrySet()) {
            if(anchors.containsKey(entry.getKey())) {
                result.put(entry.getKey(), anchors.get(entry.getKey()));
            }
        }
        Random r = new Random();
        try {
            if (result.get(Anchorpoint.BODY) == null) {
                result.put(Anchorpoint.BODY, new Armor(1L, 25.00, 50, r.nextInt(0, 40)+1));
            }
            if (result.get(Anchorpoint.BELT) == null) {
                result.put(Anchorpoint.BELT, new Purse(1.00, r.nextInt(0, 100) + 1));
            }
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
        return result;
    }

    @Override
    public boolean hasProperAnchors() {
        return super.hasProperAnchors() && getNbOfItemsOfTypeHeld(Armor.class) <= 2;
    }

    /*
        Combat
     */

    /**
     * Checks if the given protection is a valid protection for a hero.
     *
     * @param   protection
     *          The protection to check
     *
     * @return  True if and only if the given protection is valid for an entity and equal to the default protection of
     *          a hero
     *          | result == (
     *          |   super.canHaveAsBaseProtection(protection) &&
     *          |   protection == getDefaultProtection()
     *          | )
     */
    @Override
    public boolean canHaveAsBaseProtection(int protection) {
        return super.canHaveAsBaseProtection(protection) && protection == getDefaultProtection();
    }

    /**
     * Returns the default protection for a hero
     */
    @Override
    @Basic
    public int getDefaultProtection() {
        return 10;
    }

    /**
     * Returns the rolled hit value to compare to opponents protection
     * @return  A random number between 0 and 100 inclusive
     *          | result == randomint(0..101)
     */
    @Override
    protected int getHitChance() {
        return (new Random()).nextInt(0, 101);
    }

    /**
     * Returns the damage this Hero deals to an opponent
     * @return The damage this hero deals to an opponent based
     */
    @Override
    public int getDamage() {
        return Math.max(0, (int)((getStrength() + getDamageFromWeapons()-10)/2));
    }

    /**
     * Returns the damage this hero has because of its equipped weapons
     *
     * @return  The damage of the non-broken weapon(s) in the left- and/or right-hand of this hero.
     *          | result ==
     *          |   0
     *          |   if (getItemAt(Anchorpoint.LEFT_HAND) instanceof Weapon)
     *          |   then    + ((Weapon) getItemAt(Anchorpoint.LEFT_HAND)).getDamage()
     *          |   if (getItemAt(Anchorpoint.RIGHT_HAND) instanceof Weapon)
     *          |   then    + ((Weapon) getItemAt(Anchorpoint.LEFT_HAND)).getDamage()
     */
    private int getDamageFromWeapons() {
        Item leftHand = getItemAt( Anchorpoint.LEFT_HAND);
        Item rightHand = getItemAt(Anchorpoint.RIGHT_HAND);

        int damage = 0;
        if (leftHand instanceof Weapon && !leftHand.isBroken()) damage += ((Weapon) leftHand).getDamage();
        if (rightHand instanceof Weapon && !rightHand.isBroken()) damage += ((Weapon) rightHand).getDamage();
        return damage;
    }

    /**
     * Returns the total protection of this hero as tue sum of the intrinsic protection and the protection gained from
     * armor.
     *
     * @return  The sum of the base protection and the protection of equipped armors
     *          | result == getBaseProtection() + getProtectionFromArmor()
     */
    @Override
    public int getProtection() {
        return getBaseProtection() + getProtectionFromArmor();
    }

    /**
     * Returns this hero has due to Armor he is wearing.
     *
     * @return  If this hero has an Armor on its back than return the effective protection of said Armor, otherwise
     *          return 0.
     *          | if(getItemAt(Anchorpoint.BODY) instanceof Armor)
     *          | then result == ((Armor) bodyItem).getEffectiveProtection();
     *          | else result == 0
     */
    protected int getProtectionFromArmor() {
        Item bodyItem = getItemAt(Anchorpoint.BODY);
        if(bodyItem instanceof Armor) return ((Armor) bodyItem).getEffectiveProtection();
        return 0;
    }

    //TODO documentation
    //TODO implementation using randomness
    /**
     * Allows the collection of treasures from the given opponent
     *
     * @param   opponent
     *          The opponent to collect treasures from
     *
     * @effect
     * @throws  IllegalArgumentException
     *          If the given opponent is not dead
     */
    @Override
    protected void collectTreasuresFrom(Entity opponent)
            throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException, BrokenItemException {
        if(!opponent.isDead()) throw new IllegalArgumentException("The given opponent is not dead");
        if(!opponent.getAnchorPoints().isEmpty()) {
            for(Anchorpoint anchorOpp: opponent.getAnchorPoints()) {
                for (Anchorpoint anchorOwn: getAnchorPoints()) {

                }
            }
        }
    }

    /**
     * Deals the final blow to an opponent ands heals this hero.
     *
     * @param   opponent
     *          The opponent
     *
     * @effect  Deals the final blow to the opponent
     *          | super.dealFinalBlow()
     * @effect  Heals this hero
     *          | heal()
     */
    @Override
    protected void dealFinalBlow(Entity opponent) throws InvalidAnchorException, InvalidHolderException, BrokenItemException {
        super.dealFinalBlow(opponent);
        heal();
    }

    /**
     * Hit the given monster
     *
     * @param   monster
     *          The monster to hit
     *
     * @effect  Hits this monster according to the defined hitting rules
     *          | super.hit(monster)
     */
    public void hit(Monster monster)
            throws DeadEntityException, BrokenItemException, InvalidAnchorException, InvalidHolderException {
        super.hit(monster);
    }

    /**
     * Initiates a fight with the given monster
     *
     * @param   monster
     *          The monster to fight
     * @effect  Initiates a fight with the given monster
     *          | super.fight(monster)
     *
     * @note    This makes it so that heroes can only fight monsters
     */
    public void fight(Monster monster) {
        super.fight(monster);
    }


    /**
     * Heals this hero
     *
     * @effect  Sets the hit points of this hero tothe current hit points increased with a
     */
    public void heal() {
        setHitPoints(getFirstLowerPrime(
                getHitPoints() + (((new Random()).nextInt(100)+1) * (getMaxHitPoints()-getHitPoints()))/100));
    }

    /*
        Item holder
     */

    /**
     * Checks if this hero can pick up the given item
     *
     * @param   item
     *          The item to check
     *
     * @return  True if and only if the given item can be picked up as an entity and if the given item is an instance
     *          of Armor, this hero does not hold 2 or more Armors already.
     */
    @Override
    public boolean canPickup(Item item) {
        return super.canPickup(item) && !(item instanceof Armor && getNbOfItemsOfTypeHeld(Armor.class) >= 2);
    }

    /*
        Name
     */
    /**
     * Variable referencing the special characters a hero can have in its name
     */
    private static final String specialCharacters = ".&" + '"';

    /**
     * Returns the special characters a hero can have
     */
    private static String getSpecialCharacters() {
        return specialCharacters;
    }


    /**
     * Checks if the given name is a valid name for this hero
     *
     * @param   name
     *          The name to check
     *
     * @return  True if and only if the given name does not include more than two apostrophes and only consists out of
     *          letters and the special characters of a hero with the restriction that :'s must be followed by a space.
     *          | if((name.length() - name.replace("'", "").length()) > 2)
     *          | then result == false
     *          | else result == super.canHaveAsName() && name.matches("[a-zA-Z'[: ]{getSpecialCharacters}]*"
     */
    @Override
    public boolean canHaveAsName(String name) {
        if((name.length() - name.replace("'","").length()) > 2) return false;
        return super.canHaveAsName(name) && name.matches(String.format("[a-zA-Z'[: ] %s]*", getSpecialCharacters()));
    }
}