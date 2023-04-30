import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import rpg.*;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

public class HeroTest {

    @Nested
    @DisplayName("Test canHaveItemAt()")
    class TestCanHaveItemAt {

        private static Hero noItemHero;
        private static Hero deadHero;

        private static Purse purse;
        private static Purse brokenPurse;
        private static Weapon weapon;
        private static Weapon brokenWeapon;
        private static Armor armor;
        private static Armor brokenArmor;
        private static Backpack backpack;
        private static Backpack brokenBackpack;

        @BeforeAll
        static void immutableSetup() throws InvalidAnchorException, BrokenItemException, InvalidHolderException {
            noItemHero = new Hero();
            deadHero = new Hero();
            deadHero.die();

            purse = new Purse(10.00, noItemHero, 10);
            brokenPurse = new Purse(20.00, noItemHero, 10);
            brokenPurse.discard();

            weapon = new Weapon(5.00, noItemHero, 14);
            brokenWeapon = new Weapon(900, noItemHero, 21);
            brokenWeapon.discard();

            armor = new Armor(0L, 49.00, 500, noItemHero, 100);
            brokenArmor = new Armor(0L, 49.00, 500, noItemHero, 100);
            brokenArmor.discard();

            backpack = new Backpack(10.00, 10, noItemHero, 15);
            brokenBackpack = new Backpack(10.00, 10, noItemHero, 15);
            brokenBackpack.discard();
        }

        @Test
        void canHaveItemAt_Purse() {
            // Empty anchors
            assertTrue(noItemHero.canHaveItemAtAnchor(purse, "Belt"));
            assertTrue(noItemHero.canHaveItemAtAnchor(brokenPurse, "Belt"));

            assertFalse(noItemHero.canHaveItemAtAnchor(purse, "Left Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(purse, "Right Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(purse, "Back"));
            assertFalse(noItemHero.canHaveItemAtAnchor(purse, "Body"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenPurse, "Left Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenPurse, "Right Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenPurse, "Back"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenPurse, "Body"));
        }

        @Test
        void canHaveItemAt_Weapon() {
            // Empty anchors
            assertTrue(noItemHero.canHaveItemAtAnchor(weapon, "Left Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(weapon, "Right Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(weapon, "Back"));
            assertTrue(noItemHero.canHaveItemAtAnchor(weapon, "Body"));
            assertFalse(noItemHero.canHaveItemAtAnchor(weapon, "Belt"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenWeapon, "Left Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenWeapon, "Right Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenWeapon, "Back"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenWeapon, "Body"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenWeapon, "Belt"));
        }

        @Test
        void canHaveAsItemAt_Armor() {
            // Empty anchors
            assertTrue(noItemHero.canHaveItemAtAnchor(armor, "Left Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(armor, "Right Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(armor, "Back"));
            assertTrue(noItemHero.canHaveItemAtAnchor(armor, "Body"));

            assertFalse(noItemHero.canHaveItemAtAnchor(armor, "Belt"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenArmor, "Left Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenArmor, "Right Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenArmor, "Back"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenArmor, "Body"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenArmor, "Belt"));
        }

        @Test
        void canHaveAsItemAt_Backpack() {
            // Empty anchors
            assertTrue(noItemHero.canHaveItemAtAnchor(backpack, "Left Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(backpack, "Right Hand"));
            assertTrue(noItemHero.canHaveItemAtAnchor(backpack, "Back"));
            assertTrue(noItemHero.canHaveItemAtAnchor(backpack, "Body"));

            assertFalse(noItemHero.canHaveItemAtAnchor(backpack, "Belt"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenBackpack, "Left Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenBackpack, "Right Hand"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenBackpack, "Back"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenBackpack, "Body"));
            assertFalse(noItemHero.canHaveItemAtAnchor(brokenBackpack, "Belt"));
        }

        @Test
        void canHaveAsItemAt_deadHero() {

            assertTrue(deadHero.canHaveItemAtAnchor(null, "Belt"));
            assertTrue(deadHero.canHaveItemAtAnchor(null, "Back"));
            assertTrue(deadHero.canHaveItemAtAnchor(null, "Body"));
            assertTrue(deadHero.canHaveItemAtAnchor(null, "Left Hand"));
            assertTrue(deadHero.canHaveItemAtAnchor(null, "Right Hand"));

            assertFalse(deadHero.canHaveItemAtAnchor(armor, "Back"));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenArmor, "Right Hand"));

            assertFalse(deadHero.canHaveItemAtAnchor(backpack, "Belt"));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenBackpack, "Left Hand"));
        }

        @Test
        void canHaveAsItemAt_Null_Exceptions() {
            assertTrue(noItemHero.canHaveItemAtAnchor(null, "Back"));

            assertThrows(IllegalArgumentException.class, () -> deadHero.canHaveItemAtAnchor(null, null));

            assertThrows(IllegalArgumentException.class, () -> noItemHero.canHaveItemAtAnchor(weapon, null));
            assertThrows(IllegalArgumentException.class, () -> noItemHero.canHaveItemAtAnchor(armor, "Head"));
            assertThrows(IllegalArgumentException.class, () -> noItemHero.canHaveItemAtAnchor(backpack, ""));
        }
    }
}
