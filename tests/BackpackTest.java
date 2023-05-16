import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import rpg.*;
import rpg.exceptions.InvalidHolderException;

public class BackpackTest {

    private static Hero hero;

    private static Weapon weapon1;
    private static Weapon weapon2;
    private static Weapon brokenWeapon;

    private static Armor armor1;
    private static Armor armor2;

    private static Purse purse;

    private static Backpack backpack1;
    private static Backpack backpack2;


    @BeforeEach
    void setup() {
        hero = new Hero("Hero", 10); // Hero capacity is 200

        weapon1 = new Weapon(5, 7);
        weapon2 = new Weapon(400, 49);
        brokenWeapon = new Weapon(10, 49);
        try {
            brokenWeapon.discard();
        } catch (Exception e) {
            fail();
        }

        armor1 = new Armor(-1, 15, 100, 60);
        armor2 = new Armor(-1, 700, 200, 70);

        purse = new Purse(0.5, 1000);

        backpack1 = new Backpack(15, 50, 2000);
        backpack2 = new Backpack(20, 100, 3000);
        try {
            hero.pickup(backpack2);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void pickup_Legal() {
        try {
            backpack1.pickup(weapon2);
            backpack2.pickup(armor1);
            backpack2.pickup(weapon1);
        } catch (Exception e) {
            fail();
        }
        assertTrue(backpack1.holdsItemDirectly(weapon2));
        assertEquals(backpack1, weapon2.getHolder());
        assertTrue(backpack1.hasProperContents());
        assertTrue(weapon2.canHaveAsHolder(weapon2.getHolder()));

        assertTrue(backpack2.holdsItemDirectly(armor1));
        assertEquals(backpack2, armor1.getHolder());
        assertTrue(backpack2.holdsItemDirectly(weapon1));
        assertEquals(backpack2, weapon1.getHolder());
        assertTrue(backpack2.hasProperContents());
        assertTrue(armor1.canHaveAsHolder(armor1.getHolder()));
        assertTrue(weapon1.canHaveAsHolder(weapon1.getHolder()));
    }

    @Test
    void pickup_Illegal() {
        try {
            backpack1.pickup(weapon1);
        } catch (Exception e) {
            fail();
        }
        assertThrows(IllegalArgumentException.class, () -> backpack1.pickup(null));
        assertThrows(IllegalArgumentException.class, () -> backpack2.pickup(weapon1));
        assertThrows(InvalidHolderException.class, () -> backpack2.pickup(weapon2));
    }
}
