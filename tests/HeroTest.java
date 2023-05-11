import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpg.*;
import rpg.exceptions.BrokenItemException;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class HeroTest {

    @Nested
    @DisplayName("Test Constructors")
    class TestConstructor {
        private static Hero hero1;
        private static Hero hero2;

        @Test
        void HeroSimple_Legal() {
            hero1 = new Hero("First", 25.00);
            // Test given parameters
            assertEquals("First",hero1.getName());
            assertEquals(25.00, hero1.getStrength());
            assertTrue(hero1.hasAnchor(Anchorpoint.LEFT_HAND));
            assertTrue(hero1.hasAnchor(Anchorpoint.RIGHT_HAND));
            assertTrue(hero1.hasAnchor(Anchorpoint.BACK));
            assertTrue(hero1.hasAnchor(Anchorpoint.BODY));
            assertTrue(hero1.hasAnchor(Anchorpoint.BELT));
            assertNull(hero1.getItemAt(Anchorpoint.LEFT_HAND));
            assertNull(hero1.getItemAt(Anchorpoint.RIGHT_HAND));
            assertNull(hero1.getItemAt(Anchorpoint.BACK));
            assertTrue(hero1.getItemAt(Anchorpoint.BODY) instanceof Armor);
            assertTrue(hero1.getItemAt(Anchorpoint.BELT) instanceof Purse);
            assertEquals(hero1.getDefaultProtection(), hero1.getBaseProtection());
            assertEquals(100, hero1.getMaxHitPoints());
            assertEquals(Entity.getFirstLowerPrime(100), hero1.getHitPoints());
        }

        @Test
        void HeroExtensive_Legal() {

            Item weapon = new Weapon(2.00, 28);
            Item armor = new Armor(-1, 10.00, 100, 63);
            Item backpack = new Backpack(3.20, 113, 150.9);

            Collection<Anchorpoint> defaultAnchors = new HashSet<>(){{
                add(Anchorpoint.BELT);
                add(Anchorpoint.LEFT_HAND);
                add(Anchorpoint.RIGHT_HAND);
                add(Anchorpoint.BODY);
                add(Anchorpoint.BACK);
            }};
            Collection<Item> items = new HashSet<>(){{
                add(weapon);
                add(armor);
                add(backpack);
            }};
            hero1 = new Hero("Extensive", 363, Entity.getFirstLowerPrime(300), items, 15.314);
            // Check for only containing the default anchors
            for (Anchorpoint anchor: Anchorpoint.values()) {
                if(defaultAnchors.contains(anchor)) {
                    assertTrue(hero1.hasAnchor(anchor));
                } else {
                    assertFalse(hero1.hasAnchor(anchor));
                }
            }
            // Check attributes
            assertEquals("Extensive", hero1.getName());
            assertEquals(15.31, hero1.getStrength());
            assertEquals(363, hero1.getMaxHitPoints());
            assertEquals(Entity.getFirstLowerPrime(300), hero1.getHitPoints());
            assertEquals(hero1.getDefaultProtection(), hero1.getBaseProtection());
            assertEquals(hero1.getDefaultProtection() + 63, hero1.getProtection());

            // Check contents of anchors
            assertTrue(hero1.getItemAt(Anchorpoint.BODY) instanceof Armor);
            assertTrue(hero1.getItemAt(Anchorpoint.BELT) instanceof Purse);
            assertTrue(hero1.holdsItemDirectly(weapon));
            assertTrue(hero1.holdsItemDirectly(armor));
            assertTrue(hero1.holdsItemDirectly(backpack));

            // Check invariants
            assertTrue(hero1.canHaveAsName(hero1.getName()));
            assertTrue(hero1.canHaveAsMaxHitPoints(hero1.getMaxHitPoints()));
            assertTrue(hero1.canHaveAsHitPoints(hero1.getHitPoints()));
            assertTrue(hero1.hasProperAnchors());
            assertTrue(hero1.canHaveAsBaseProtection(hero1.getBaseProtection()));
            assertTrue(hero1.canHaveAsCapacity(hero1.getCapacity()));
            assertTrue(hero1.canHaveAsStrength(hero1.getStrength()));

        }

        @Test
        void HeroExtensive_Illegal() {
            Item weapon = new Weapon(2.00, 28);
            Item weapon2 = new Weapon(1, 7);
            Item armor = new Armor(-1, 10.00, 100, 63);
            Item backpack = new Backpack(3.20, 113, 150.9);
            Item backpack2 = new Backpack(2.53, 67, 25.3);
            Item purse = new Purse(0.05, 50);
            Item purse2 = new Purse(0.04, 1000);
            Collection<Item> items = new HashSet<>(){{
                add(weapon);
                add(armor);
                add(backpack);
            }};

            // Too many purses
            Collection<Item> items2 = new HashSet<>(items){{
                add(purse);
                add(purse2);
            }};

            // Too many items
            Collection<Item> items3 = new HashSet<>(items){{
                add(weapon2);
                add(purse);
                add(backpack2);
            }};
            // Name
            assertThrows(IllegalArgumentException.class,
                    ()->new Hero("illegalName", 363, Entity.getFirstLowerPrime(300), items, 15.314));
            assertThrows(IllegalArgumentException.class,
                    ()->new Hero("0illegalName", 363, Entity.getFirstLowerPrime(300), items, 15.314));
            assertThrows(IllegalArgumentException.class,
                    ()->new Hero("Illegal:Name", 363, Entity.getFirstLowerPrime(300), items, 15.314));
            // Not able to allocate all items
            assertThrows(IllegalArgumentException.class,
                    ()->new Hero("LegalName", 363, Entity.getFirstLowerPrime(300), items2, 15.314));
            assertThrows(IllegalArgumentException.class,
                    ()->new Hero("LegalName", 363, Entity.getFirstLowerPrime(300), items3, 15.314));

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
        static void immutableSetup() throws BrokenItemException {
            purse = new Purse(10.00, 10);
            brokenPurse = new Purse(20.00,  10);

            weapon = new Weapon(5.00,  14);
            brokenWeapon = new Weapon(900,  21);

            armor = new Armor(0L, 49.00, 500,  100);
            brokenArmor = new Armor(0L, 49.00, 500,  100);

            backpack = new Backpack(10.00, 10,  15);
            brokenBackpack = new Backpack(10.00, 10,  15);

            aliveHero = new Hero("Alive", 100, 97, new HashSet<>(){{
                add(purse);
                add(weapon);
                add(armor);
                add(backpack);
            }}, 50);

            deadHero = new Hero("Alive", 100, 97, new HashSet<>(){{
                add(brokenPurse);
                add(brokenWeapon);
                add(brokenArmor);
                add(brokenBackpack);
            }}, 50);
            brokenPurse.discard(); // Also drops brokenPurse
            brokenWeapon.discard();
            brokenArmor.discard();
            brokenBackpack.discard();
            deadHero.die();

        }

        @Test
        void canHaveItemAt_Purse() {
            // Empty anchors
            for (Anchorpoint anchor: aliveHero.getAnchorPoints()) {
                if(anchor == Anchorpoint.BELT) {
                    assertTrue(aliveHero.canHaveItemAtAnchor(purse, anchor));
                } else {
                    assertFalse(aliveHero.canHaveItemAtAnchor(purse, anchor));
                }
                // anchor == BELT should also be evaluated as false since it should hold purse already
                assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, anchor));
            }
        }

        @Test
        void canHaveItemAt_Weapon() {
            for (Anchorpoint anchor: aliveHero.getAnchorPoints()) {
                if (anchor != Anchorpoint.BELT && aliveHero.getItemAt(anchor) == null ||
                        aliveHero.getItemAt(anchor) == weapon) {
                    assertTrue(aliveHero.canHaveItemAtAnchor(weapon, anchor));
                } else {
                    assertFalse(aliveHero.canHaveItemAtAnchor(weapon, anchor));
                }
                assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, anchor));
            }
        }

        @Test
        void canHaveAsItemAt_Armor() {

            for (Anchorpoint anchor: aliveHero.getAnchorPoints()) {
                if (anchor != Anchorpoint.BELT && aliveHero.getItemAt(anchor) == null ||
                        aliveHero.getItemAt(anchor) == armor) {
                    assertTrue(aliveHero.canHaveItemAtAnchor(armor, anchor));
                } else {
                    assertFalse(aliveHero.canHaveItemAtAnchor(armor, anchor));
                }
                assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, anchor));
            }
        }

        @Test
        void canHaveAsItemAt_Backpack() {

            for (Anchorpoint anchor: aliveHero.getAnchorPoints()) {
                if (anchor != Anchorpoint.BELT && aliveHero.getItemAt(anchor) == null ||
                        aliveHero.getItemAt(anchor) == backpack) {
                    assertTrue(aliveHero.canHaveItemAtAnchor(backpack, anchor));
                } else {
                    assertFalse(aliveHero.canHaveItemAtAnchor(backpack, anchor));
                }
                assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, anchor));
            }
        }

        @Test
        void canHaveAsItemAt_deadHero() {

            for (Anchorpoint anchor: deadHero.getAnchorPoints()) {
                assertTrue(deadHero.canHaveItemAtAnchor(null, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(armor, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(brokenArmor, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(weapon, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(brokenWeapon, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(purse, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(brokenPurse, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(backpack, anchor));
                assertFalse(deadHero.canHaveItemAtAnchor(brokenBackpack, anchor));

            }
        }

        @Test
        void canHaveAsItemAt_Null_Exceptions() {
            assertTrue(aliveHero.canHaveItemAtAnchor(null, Anchorpoint.RIGHT_HAND));

            assertThrows(IllegalArgumentException.class, () -> deadHero.canHaveItemAtAnchor(null, null));

            assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(weapon, null));
            for (Anchorpoint anchor: Anchorpoint.values()) {
                if (!aliveHero.hasAnchor(anchor)) {
                    assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(armor, anchor));
                }
            }
        }
    }
}
