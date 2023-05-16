import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rpg.Armor;
import rpg.Item;

import static org.junit.jupiter.api.Assertions.*;

public class ArmorTest {

    private static Armor armor1;

    @BeforeAll
    static void immutableSetup() {
        Armor.addArmorType("TestType", 32);
    }

    @BeforeEach
    void setup() {
        armor1 = new Armor(2L, 25.00, 100, 50);
    }

    @Test
    void constructorTest() {
        assertEquals(2L, armor1.getId());
        assertTrue(armor1.canHaveAsId(armor1.getId()));
        assertEquals(25.00, armor1.getWeight());
        assertTrue(Item.isValidWeight(armor1.getWeight()));
        assertEquals(100, armor1.getValue());
        assertEquals(100, armor1.getMaxValue());
        assertTrue(armor1.canHaveAsValue(armor1.getValue()));
        assertEquals(50, armor1.getMaxProtection());
        assertTrue(Armor.isValidMaxProtection(armor1.getMaxProtection()));
        assertEquals(armor1.getMaxProtection(), armor1.getEffectiveProtection());
    }

    @Test
    void test() {

    }
}
