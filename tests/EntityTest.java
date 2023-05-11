import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import rpg.*;

public class EntityTest {

    private static Hero hero;

    private static Monster monster;

    private static Weapon weapon;
    private static Armor armor;
    private static Backpack backpack;
    private static Purse purse;


    @BeforeEach
    void setup() {
        hero = new Hero("Human: World's Hero", 25.30);
        monster = new Monster("Goblin", 130, 10, 50);


    }

    @Test
    void hit_Legal() {

    }

    @Test
    void hit_Illegal() {

    }

    @Test
    void drop_Legal() {

    }

    @Test
    void drop_Illegal() {

    }
}
