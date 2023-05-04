package rpg;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import rpg.exceptions.BrokenItemException;
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
     *          | this(name, 100, getFirstLowerPrime(100), getInitializedAnchors(defaultAnchors), strength)
     */
    public Hero(String name, double strength)
            throws InvalidAnchorException, BrokenItemException, InvalidHolderException {
        this(name, 100, getFirstLowerPrime(100), defaultAnchors, strength);
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
     * @effect  Initializes this hero with the given name, maximum hit points and initialized anchors using
     *          the given anchors and default protection (?).
     *          | super(name, maxHitPoints, hitPoints, getInitializedAnchors(anchors), -1)
     * @post    The strength of this hero is set to the given strength
     *          | setStrength(strength)
     */
    //TODO check if effect super() is correct
    public Hero(String name, int maxHitPoints, int hitPoints, Map<Anchor, Item> anchors, double strength)
            throws BrokenItemException, InvalidAnchorException, InvalidHolderException {
        super(name, maxHitPoints, hitPoints, -1);
        setStrength(strength);
        setAnchors(getInitializedAnchors(anchors));
    }


    /*
        Strength (TOTAL)
     */
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
     * Sets the intrinsic strength of the hero to
     */
    @Model
    protected void setStrength(double strength) {
        if(canHaveAsStrength(strength)) {
           this.strength = strength;
        } else {
            this.strength = getDefaultStrength();
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
     *          | setStrength(?)
     */
    // TODO ? formal
    public void multiplyStrength(int factor) {
        if(factor > 0) {
            BigDecimal db = new BigDecimal(getStrength() * factor).setScale(2, RoundingMode.HALF_UP);
            setStrength(db.doubleValue());
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
     *          | setStrength(?)
     */
    //TODO ? formal
    public void divideStrength(int factor) {
        if(factor > 0) {
            BigDecimal db = new BigDecimal(getStrength() / factor).setScale(2, RoundingMode.HALF_UP);
            setStrength(db.doubleValue());
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

    /**
     * Returns the capacity a Hero can carry, calculated by using strength
     *
     * @param   strength
     *          The given strength
     * @return  The capacity calculated from the strength
     *          | result == (20 * strength)
     */
    private static double getCapacityFromStrength(double strength) {
        return 20*strength;
    }

    /**
     * Variable referencing the default anchors of a Hero
     */
    private static HashMap<Anchor, Item> defaultAnchors = new HashMap<>() {{
        put(Anchor.LEFT_HAND, null);
        put(Anchor.RIGHT_HAND, null);
        put(Anchor.BODY, null);
        put(Anchor.BACK, null);
        put(Anchor.BELT, null);
    }};

    //TODO documentation
    /**
     * @param anchors
     * @return
     */
    @Raw
    private static Map<Anchor, Item> getInitializedAnchors(Map<Anchor, Item> anchors) {
        Map<Anchor, Item> result = new HashMap<>(defaultAnchors);
        for(Map.Entry<Anchor, Item> entry: result.entrySet()) {
            if(anchors.containsKey(entry.getKey())) {
                result.put(entry.getKey(), anchors.get(entry.getKey()));
            }
        }
        Random r = new Random();
        try {
            if (result.get(Anchor.BODY) == null) {
                result.put(Anchor.BODY, new Armor(1L, 25.00, 50, null, 60));
            }
            if (result.get(Anchor.BELT) == null) {
                result.put(Anchor.BELT, new Purse(1.00, null, 100, r.nextInt(0, 100) + 1));
            }
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
        return result;
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
    protected int getDefaultProtection() {
        return 10;
    }

    @Override
    protected int getHitChance() {
        return (new Random()).nextInt(0, 101);
    }

    @Override
    protected int getDamage() {
        return Math.max(0, (int)((getStrength() + getDamageFromWeapons()-10)/2));
    }

    private int getDamageFromWeapons() {
        Item leftHand = getItemAt( Anchor.LEFT_HAND);
        Item rightHand = getItemAt(Anchor.RIGHT_HAND);

        int damage = 0;
        if (leftHand instanceof Weapon) damage += ((Weapon) leftHand).getDamage();
        if (rightHand instanceof Weapon) damage += ((Weapon) rightHand).getDamage();
        return damage;
    }

    @Override
    protected int getProtection() {
        return 0;
    }

    @Override
    protected void collectTreasuresFrom(Entity opponent)
            throws IllegalArgumentException, InvalidAnchorException, InvalidHolderException {
        if(!opponent.isDead()) throw new IllegalArgumentException("The given opponent is not dead");
        if(!opponent.getAnchorPoints().isEmpty()) {
            System.out.println("Choose the treasures you wish to collect by entering the desired anchors. \n " +
                    "Quit by entering Q");
            for (Anchor anchor: getAnchorPoints()) {
                System.out.printf("%s: %s", anchor, opponent.getItemAt(anchor));
            }
            Scanner sc = new Scanner(System.in);
            Anchor oppAnchor = Anchor.get(sc.nextLine());
            Anchor ownAnchor;
            while(oppAnchor != null) {
                if(!hasAnchor(oppAnchor))
                    throw new IllegalArgumentException("The given anchor does not exist");
                ownAnchor = Anchor.get(sc.nextLine());
                opponent.transferItemAtAnchorTo(this, oppAnchor, ownAnchor);
            }

        }


    }

    /**
     * Initiates a fight with the given monster
     *
     * @param   monster
     *          The monster to fight
     * @effect  Initiates a fight with the given monster
     *          | super.fight((Entity) monster)
     *
     * @note    This makes it so that heroes can only fight monsters
     */
    public void fight(Monster monster) {
        super.fight((Entity) monster);
    }
}