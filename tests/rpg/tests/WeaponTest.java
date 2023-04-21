package rpg.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import rpg.*;
import rpg.exceptions.BrokenEquipmentException;
import rpg.exceptions.InvalidHolderException;

class WeaponTest {

    private static Equipment weapon;
    private static Equipment backPack;
    private static Entity entity;

    private static Equipment degradableWeapon;


    @BeforeAll
    public static void immutableSetup() throws BrokenEquipmentException, InvalidHolderException {
        entity = new Hero();
        backPack = new Backpack(35.00, 200, entity, 100.00);
        weapon = new Weapon(-1.0, (EquipmentHolder) backPack, 25);
    }

    @BeforeEach
    void setUp() throws BrokenEquipmentException, InvalidHolderException {
        degradableWeapon = new Weapon(25.16, (EquipmentHolder) entity, 35);
    }
}