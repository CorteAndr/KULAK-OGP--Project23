package rpg;

public class Main {
    public static void main(String[] args) {

        Hero hero = new Hero();
        System.out.println(hero.getValueOfPossessions());

        Monster monster = new Monster();
        System.out.println(monster.getValueOfPossessions());


    }
}
