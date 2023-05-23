import java.util.*;
public class Dice {
    private Random rng;
    private HashMap<Integer, Integer> probMap;

    public Dice(long seed) {
        rng = new Random(seed);
        probMap = new HashMap<>();

        // Duplicate rolls.
        probMap.put(0, 2);
        probMap.put(1, 4);
        probMap.put(2, 6);
        probMap.put(3, 8);
        probMap.put(4, 10);
        probMap.put(5, 12);

        // Valid rolls.
        probMap.put(6, 3);
        probMap.put(7, 3);
        probMap.put(8, 4);
        probMap.put(9, 4);
        probMap.put(10, 5);
        probMap.put(11, 5);
        probMap.put(12, 5);
        probMap.put(13, 5);
        probMap.put(14, 6);
        probMap.put(15, 6);
        probMap.put(16, 6);
        probMap.put(17, 6);
        probMap.put(18, 7);
        probMap.put(19, 7);
        probMap.put(20, 7);
        probMap.put(21, 7);
        probMap.put(22, 7);
        probMap.put(23, 7);
        probMap.put(24, 8);
        probMap.put(25, 8);
        probMap.put(26, 8);
        probMap.put(27, 8);
        probMap.put(28, 9);
        probMap.put(29, 9);
        probMap.put(30, 9);
        probMap.put(31, 9);
        probMap.put(32, 10);
        probMap.put(33, 10);
        probMap.put(34, 11);
        probMap.put(35, 11);
    }

    /**
     * Returns the sum of two dice rolls. If both die show
     * the same value, then they are re-rolled with the
     * duplicate values added on. If duplicates are obtained
     * for three rolls, then return -1 to indicate Go-to-jail.
     *
     * @return Next roll value (-1 if it is Go-to-jail).
     */
    public int roll() {
        boolean duplicate = true;
        int attempt = 0;
        int value = 0;
        while (duplicate && attempt < 3) {
            attempt++;
            int rand = rng.nextInt(36);
            int roll = probMap.get(rand);
            value += roll;
            if (rand >= 6) duplicate = false;
        }
        return !duplicate ? value : -1;
    }

    public static void main(String[] args) {
//        Dice die = new Dice(1);
//        for (int i = 0; i < 500; i++) {
//            System.out.println(die.roll());
//        }
    }
}
