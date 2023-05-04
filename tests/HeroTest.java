import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import rpg.*;
import rpg.exceptions.BrokenItemException;
import rpg.exceptions.InvalidAnchorException;
import rpg.exceptions.InvalidHolderException;

import java.util.HashMap;
import java.util.Map;

public class HeroTest {


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
            purse = new Purse(10.00, null, 10);
            brokenPurse = new Purse(20.00, null, 10);

            weapon = new Weapon(5.00, null, 14);
            brokenWeapon = new Weapon(900, null, 21);

            armor = new Armor(0L, 49.00, 500, null, 100);
            brokenArmor = new Armor(0L, 49.00, 500, null, 100);

            backpack = new Backpack(10.00, 10, null, 15);
            brokenBackpack = new Backpack(10.00, 10, null, 15);

            aliveHero = new Hero("Alive", 100, 97, new HashMap<Anchor, Item>(){{
                put(Anchor.BELT, purse);
                put(Anchor.LEFT_HAND, weapon);
                put(Anchor.BODY, armor);
                put(Anchor.BACK, backpack);
            }}, 50);

            deadHero = new Hero("Alive", 100, 97, new HashMap<Anchor, Item>(){{
                put(Anchor.BELT, brokenPurse);
                put(Anchor.LEFT_HAND, brokenWeapon);
                put(Anchor.BODY, brokenArmor);
                put(Anchor.BACK, brokenBackpack);
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
            assertTrue(aliveHero.canHaveItemAtAnchor(purse, Anchor.BELT));

            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchor.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchor.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(purse, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchor.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenPurse, Anchor.BODY));
        }

        @Test
        void canHaveItemAt_Weapon() {
            assertTrue(aliveHero.canHaveItemAtAnchor(weapon, Anchor.LEFT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(weapon, Anchor.RIGHT_HAND));

            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(weapon, Anchor.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchor.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenWeapon, Anchor.BELT));
        }

        @Test
        void canHaveAsItemAt_Armor() {

            assertTrue(aliveHero.canHaveItemAtAnchor(armor, Anchor.RIGHT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(armor, Anchor.BODY));

            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(armor, Anchor.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchor.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenArmor, Anchor.BELT));
        }

        @Test
        void canHaveAsItemAt_Backpack() {

            assertTrue(aliveHero.canHaveItemAtAnchor(backpack, Anchor.RIGHT_HAND));
            assertTrue(aliveHero.canHaveItemAtAnchor(backpack, Anchor.BACK));


            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchor.BELT));
            assertFalse(aliveHero.canHaveItemAtAnchor(backpack, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchor.LEFT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchor.RIGHT_HAND));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchor.BACK));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchor.BODY));
            assertFalse(aliveHero.canHaveItemAtAnchor(brokenBackpack, Anchor.BELT));
        }

        @Test
        void canHaveAsItemAt_deadHero() {

            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchor.BELT));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchor.BACK));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchor.BODY));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchor.LEFT_HAND));
            assertTrue(deadHero.canHaveItemAtAnchor(null, Anchor.RIGHT_HAND));

            assertFalse(deadHero.canHaveItemAtAnchor(armor, Anchor.BACK));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenArmor, Anchor.RIGHT_HAND));

            assertFalse(deadHero.canHaveItemAtAnchor(backpack, Anchor.BELT));
            assertFalse(deadHero.canHaveItemAtAnchor(brokenBackpack, Anchor.LEFT_HAND));
        }

        @Test
        void canHaveAsItemAt_Null_Exceptions() {
            assertTrue(aliveHero.canHaveItemAtAnchor(null, Anchor.RIGHT_HAND));

            assertThrows(IllegalArgumentException.class, () -> deadHero.canHaveItemAtAnchor(null, null));

            assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(weapon, null));
            assertThrows(IllegalArgumentException.class, () -> aliveHero.canHaveItemAtAnchor(armor, Anchor.HEAD));
        }
    }
}
