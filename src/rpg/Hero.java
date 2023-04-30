package rpg;

//TODO Everything

import be.kuleuven.cs.som.annotate.Model;
import rpg.exceptions.InvalidAnchorException;

import java.util.HashMap;

/**
 * Class of Heroes
 *
 * @author  Corteville Andrew
 */
public class Hero extends Entity {


    //TODO Remove this constructor
    public Hero() throws InvalidAnchorException {
        super("Tester", 100, defaultAnchors);
        setStrength(100);
    }

    public Hero(String name, double strength) throws InvalidAnchorException {
        super(name, 100);
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
        if(isValidStrength(strength)) {
           this.strength = strength;
        } else {
            this.strength = getDefaultStrength();
        }
    }

    /**
     * Checks if the given strength is a valid strength for a Hero
     *
     * @param   strength
     *          The strength to check
     *
     * @return  True if and only if the given strength is a strictly positive double, otherwise return false.
     *          | result == (strength > 0)
     */
    public static boolean isValidStrength(double strength) {
        return strength > 0;
    }

    private static double getDefaultStrength() {
        return 15.00;
    }


    @Override
    public double getCapacity() {
        return getStrength() * 20;
    }


    private static HashMap<String, Item> defaultAnchors = new HashMap<>() {{
        put("Left Hand", null);
        put("Right Hand", null);
        put("Body", null);
        put("Back", null);
        put("Belt", null);
    }};
}