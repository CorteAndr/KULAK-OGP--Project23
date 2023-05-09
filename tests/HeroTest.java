import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import rpg.*;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.HashMap;

public class HeroTest {

    @Nested
    @DisplayName("Test Constructors")
    class TestConstructor {
        private static Hero hero1;
        private static Hero hero2;

        @Test
        void HeroSimple_Legal() throws IllegalArgumentException, BrokenItemException, InvalidAnchorException, InvalidHolderException {
            hero1 = new Hero("First", 25.00);
            // Test given parameters
            assertEquals("First",hero1.getName());
            assertEquals(25.00, hero1.getStrength());
            assertNull(hero1.getItemAt(Anchorpoint.LEFT_HAND));
            assertNull(hero1.getItemAt(Anchorpoint.RIGHT_HAND));
            assertNull(hero1.getItemAt(Anchorpoint.BACK));
            assertEquals(Armor.class, hero1.getItemAt(Anchorpoint.BODY).getClass());
            assertEquals(Purse.class, hero1.getItemAt(Anchorpoint.BELT).getClass());
            assertEquals(hero1.getDefaultProtection(), hero1.getBaseProtection());
            assertEquals(100, hero1.getMaxHitPoints());
            assertEquals(Entity.getFirstLowerPrime(100), hero1.getHitPoints());
        }

        @Test
        void HeroExtensive_Legal() {

        }
    }

    @Nested
    @DisplayName("Test canHaveItemAt()")
    class TestCanHaveItemAt {

        private static Hero aliveHero;
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
            purse = new Purse(10.00, 10);
            brokenPurse = new Purse(20.00,  10);

            weapon = new Weapon(5.00,  14);
            brokenWeapon = new Weapon(900,  21);

            armor = new Armor(0L, 49.00, 500,  100);
            brokenArmor = new Armor(0L, 49.00, 500,  100);

            backpack = new Backpack(10.00, 10,  15);
            brokenBackpack = new Backpack(10.00, 10,  15);

            aliveHero = new Hero("Alive", 100, 97, new HashMap<Anchorpoint, Item>(){{
                put(Anchorpoint.BELT, purse);
                put(Anchorpoint.LEFT_HAND, weapon);
                put(Anchorpoint.BODY, armor);
                put(Anchorpoint.BACK, backpack);
            }}, 50);

            deadHero = new Hero("Alive", 100, 97, new HashMap<Anchorpoint, Item>(){{
                put(Anchorpoint.BELT, brokenPurse);
                put(Anchorpoint.LEFT_HAND, brokenWeapon);
                put(Anchorpoint.BODY, brokenArmor);
                put(Anchorpoint.BACK, brokenBackpack);
            }}, 50);
            brokenPurse.discard();
            brokenWeapon.discard();
            brokenArmor.discard();
            brokenBackpack.discard();
            deadHero.die();

        }

        @Test
        void canHaveItemAt_Purse() {
            // Empty anchors
            assertTrue(aliveHero.canHaveItemAtAnchor(purse, Anchorpoint.BELT));

            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchorpoint.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchorpoint.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchorpoint.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchorpoint.BODY));
        }

        @Test
        void canHaveItemAt_Weapon() {
            assertTrue(aliveHero.canHaveItemAtAnchor(weapon, Anchorpoint.LEFT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(weapon, Anchorpoint.RIGHT_HAND));

            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchorpoint.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchorpoint.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchorpoint.BELT));
        }

        @Test
        void canHaveAsItemAt_Armor() {

            assertTrue(aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.RIGHT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.BODY));

            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.BELT));
        }

        @Test
        void canHaveAsItemAt_Backpack() {

            assertTrue(aliveHero.canHaveItemAtAnchor(backpack, Anchorpoint.RIGHT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(backpack, Anchorpoint.BACK));


            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchorpoint.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.BELT));
        }

        @Test
        void canHaveAsItemAt_deadHero() {

            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchorpoint.BELT));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchorpoint.BACK));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchorpoint.BODY));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchorpoint.LEFT_HAND));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchorpoint.RIGHT_HAND));

            assertFalse(deadHero.canHaveItemAtAnchor(armor, Anchorpoint.BACK));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenArmor, Anchorpoint.RIGHT_HAND));

            assertFalse(deadHero.canHaveItemAtAnchor(backpack, Anchorpoint.BELT));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenBackpack, Anchorpoint.LEFT_HAND));
        }

        @Test
        void canHaveAsItemAt_Null_Exceptions() {
            assertTrue(aliveHero.canHaveItemAtAnchor(null, Anchorpoint.RIGHT_HAND));

            assertThrows(IllegalArgumentException.class, () -> deadHero.canHaveItemAtAnchor(null, null));

            assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(weapon, null));
            assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(armor, Anchorpoint.HEAD));
        }
    }
}
