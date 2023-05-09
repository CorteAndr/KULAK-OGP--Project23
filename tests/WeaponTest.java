import org.junit.jupiter.api.*;
import rpg.Item;
import rpg.Weapon;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidHolderException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest {

    private static Weapon weapon1;
    private static Weapon weapon2;
    private static Weapon brokenWeapon;

    @BeforeEach
    void testConstructors() {
        // Non-null holders are tested in item holder tests
        try {
            weapon1 = new Weapon(25.00, null, 21);
        } catch (Exception e) {
            // Should not happen here
            assert false;
        }

        assertEquals(0, weapon1.getId() % 6);
        assertTrue(weapon1.canHaveAsId(weapon1.getId()));
        assertEquals(25.00, weapon1.getWeight());
        assertTrue(Item.isValidWeight(weapon1.getWeight()));
        assertEquals(21, weapon1.getDamage());
        assertTrue(Weapon.isValidDamage(weapon1.getDamage()));
        assertEquals(Weapon.getValueFromDamage(21), weapon1.getValue());
        assertNull(weapon1.getHolder());
        assertTrue(weapon1.canHaveAsHolder(weapon1.getHolder()));
        assertFalse(weapon1.isBroken());

        weapon2 = new Weapon(-36.00, 49);

        assertEquals(0, weapon2.getId() % 6);
        assertNotEquals(weapon1.getId(), weapon2.getId());
        assertNotEquals(36.00, weapon2.getWeight());
        assertEquals(Item.getDefaultWeight(), weapon2.getWeight());
        assertEquals(49, weapon2.getDamage());
        assertEquals(Weapon.getValueFromDamage(49), weapon2.getValue());
        assertNull(weapon2.getHolder());
        assertFalse(weapon2.isBroken());

        brokenWeapon = new Weapon(12.53, 14);
        try {
            brokenWeapon.discard();
        } catch (Exception e) {
            // Should not happen
            assert false;
        }
        assertEquals(0, brokenWeapon.getId() % 6);
        assertNotEquals(weapon1.getId(), brokenWeapon.getId());
        assertNotEquals(weapon2.getId(), brokenWeapon.getId());
        assertEquals(12.53, brokenWeapon.getWeight());
        assertEquals(14, brokenWeapon.getDamage());
        assertEquals(Weapon.getValueFromDamage(14), brokenWeapon.getValue());
        assertNull(brokenWeapon.getHolder());
        assertTrue(brokenWeapon.isBroken());

        assertThrowsExactly(AssertionError.class, () -> brokenWeapon = new Weapon(-2, 15));
        assertNotNull(brokenWeapon);
    }

    @Test
    void degrade_Legal() {
        try {
            weapon1.degrade(7);
            assertEquals(14, weapon1.getDamage());
            weapon1.degrade(14);
            assertEquals(14, weapon1.getDamage());
            assertTrue(weapon1.isBroken());
            weapon2.degrade(90);
            assertEquals(49, weapon2.getDamage());
            assertTrue(weapon2.isBroken());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void degrade_Illegal() {
        assertThrowsExactly(AssertionError.class, () -> weapon2.degrade(3));
        assertThrowsExactly(BrokenItemException.class, () -> brokenWeapon.degrade(20));
    }

    @Test
    void repair_Legal() {
        try {
            weapon1.repair(21);
            assertEquals(42, weapon1.getDamage());
            weapon1.repair(56);
            assertEquals(98, weapon1.getDamage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void repair_Illegal() {
        assertThrowsExactly(AssertionError.class, () -> weapon2.repair(103));
        assertThrowsExactly(BrokenItemException.class, ()-> brokenWeapon.repair(14));
    }

    @Test
    void getShiny() {
        assertTrue((weapon1.getValue() + 10 <= weapon1.getShiny()));
        assertTrue((weapon1.getValue() + 50) > weapon1.getShiny());

        assertTrue((weapon2.getValue() + 10 <= weapon2.getShiny()));
        assertTrue((weapon2.getValue() + 50) > weapon2.getShiny());
    }
}
