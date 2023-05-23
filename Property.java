import java.util.*;
public class Property extends Square{
    private static final int[] cost = new int[] {60, 60, 100, 100, 120, 140, 140, 160, 180, 180, 200, 220, 220, 240, 260, 260, 280, 300, 300, 320, 350, 400};
    private static final int[][] rent = new int[][] {
            new int[] {2, 4, 6, 6, 8, 10, 10, 12, 14, 14, 16, 18, 18, 20, 22, 22, 24, 26, 26, 28, 35, 50},
            new int[] {10, 20, 30, 30, 40, 50, 50, 60, 70, 70, 80, 90, 90, 100, 110, 110, 120, 130, 130, 150, 175, 200},
            new int[] {30, 60, 90, 90, 100, 150, 150, 180, 200, 200, 220, 250, 250, 300, 330, 330, 360, 390, 390, 450, 500, 600},
            new int[] {90, 180, 270, 270, 300, 450, 450, 500, 550, 550, 600, 700, 700, 750, 800, 800, 850, 900, 900, 1000, 1100, 1400},
            new int[] {160, 320, 400, 400, 450, 625, 625, 700, 750, 750, 800, 875, 875, 925, 975, 975, 1025, 1100, 1100, 1200, 1300, 1700},
            new int[] {250, 450, 550, 550, 600, 750, 750, 900, 950, 950, 1000, 1050, 1050, 1100, 1150, 1150, 1200, 1275, 1275, 1400, 1500, 2000}
    };
    private static final int[] houseCost = new int[] {50, 50, 50, 50, 50, 100, 100, 100, 100, 100, 100, 150, 150, 150, 150, 150, 150, 200, 200, 200, 200, 200};
    private static Map<Integer, int[]> sets;
    private static Account[] owned = new Account[22];
    private static int[] house = new int[22];
    private static boolean[] completedSet = new boolean[22];
    private static boolean[] built = new boolean[22];
    private int id;
    public Property(int id) {
        this.id = id;
        if (this.sets == null) {
            this.sets = new HashMap<>();
            int[] brown = new int[] {0, 1};
            int[] blue = new int[] {2, 3, 4};
            int[] pink = new int[] {5, 6, 7};
            int[] orange = new int[] {8, 9, 10};
            int[] red = new int[] {11, 12, 13};
            int[] yellow = new int[] {14, 15, 16};
            int[] green = new int[] {17, 18, 19};
            int[] navy = new int[] {20, 21};
            sets.put(0, brown);
            sets.put(1, brown);
            sets.put(2, blue);
            sets.put(3, blue);
            sets.put(4, blue);
            sets.put(5, pink);
            sets.put(6, pink);
            sets.put(7, pink);
            sets.put(8, orange);
            sets.put(9, orange);
            sets.put(10, orange);
            sets.put(11, red);
            sets.put(12, red);
            sets.put(13, red);
            sets.put(14, yellow);
            sets.put(15, yellow);
            sets.put(16, yellow);
            sets.put(17, green);
            sets.put(18, green);
            sets.put(19, green);
            sets.put(20, navy);
            sets.put(21, navy);
        }
    }

    @Override
    public boolean purchase(Account account) {
        // Checks for illegal purchase.
        if (this.owned[this.id] != null) {
            System.out.println(String.format("Unable to purchase Property %d: Already owned by %s", this.id, this.owned[this.id]));
            return false;
        } else if (!account.deduct(this.cost[this.id])) {
            System.out.println(String.format("Unable to purchase Property %d: Insufficient funds by %s", this.id, account));
            return false;
        }

        // Temporarily assigns @account the owner.
        this.owned[this.id] = account;

        // Verifies that the set belongs to the same Owner
        int[] group = sets.get(this.id);
        for (int i : group) {
            if (this.owned[i] != null && !account.equals(this.owned[i])) {
                // Revert all changes if already partially owned by another account.
                this.owned[this.id] = null;
                account.receive(this.cost[this.id]);
                System.out.println(String.format("Unable to purchase Property %d: Partial set owned by %s", this.id, this.owned[i]));
                return false;
            }
        }

        // Checks if every Property in the set is bought.
        boolean allBought = true;
        for (int i : group) {
            allBought = allBought && account.equals(this.owned[i]);
        }

        // Updates @completedSet if all properties in the set have been purchased.
        if (allBought) {
            for (int i : group) {
                this.completedSet[i] = true;
            }
        }

        return true;
    }

    @Override
    public Account getOwner() {
        return this.owned[this.id];
    }

    public boolean buildHouse(Account account) {
        // Checks for illegal house building.
        if (this.house[this.id] >= 5) {
            System.out.println(String.format("Unable to build on Property %d: Maximum house limit reached", this.id));
            return false;
        } else if (!this.owned[this.id].equals(account)) {
            System.out.println(String.format("Unable to build on Property %d: Owned by another player", this.id));
            return false;
        } else if (!this.completedSet[this.id]) {
            System.out.println(String.format("Unable to build on Property %d: Incomplete property set", this.id));
            return false;
        } else if (this.built[this.id]) {
            System.out.println(String.format("Unable to build on Property %d: Uneven construction", this.id));
            return false;
        } else if (!this.owned[this.id].deduct(this.houseCost[this.id])) {
            System.out.println(String.format("Unable to build on Property %d: Insufficient funds by %s", this.id, this.owned[this.id]));
            return false;
        }

        // Updates arrays.
        this.house[this.id]++;
        this.built[this.id] = true;

        int[] group = sets.get(this.id);

        // Checks if a new level of houses can be built.
        boolean completed = true;
        for (int i : group) {
            completed = completed && this.built[i];
        }

        if (completed) {
            for (int i : group) {
                this.built[i] = false;
            }
        }

        return true;
    }

    @Override
    public boolean visit(Account account, int jumps) {
        return account.pay(this.rent[this.house[this.id]][this.id], this.owned[this.id]);
    }

    @Override
    public void debug() {
        System.out.println("Debugging Property:");
        System.out.println("owned: " + Arrays.toString(this.owned));
        System.out.println("completedSet: " + Arrays.toString(this.completedSet));
        System.out.println("built: " + Arrays.toString(this.built));
        System.out.println("house: " + Arrays.toString(this.house));
        System.out.println("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof Property) {
            Property property = (Property) obj;
            // Could implement in the future
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "Property " + this.id;
    }

    public static void main(String[] args) {
//        Account p1 = new Player(1, null);
//        Account p2 = new Player(2, null);
//        Property h1 = new Property(5);
//        Property h2 = new Property(6);
//        Property h3 = new Property(7);
//        h1.purchase(p1);
//        h2.purchase(p1);
//        h3.purchase(p1);
//        System.out.println(Arrays.toString(owned));
//        System.out.println(Arrays.toString(completedSet));
//        System.out.println(Arrays.toString(built));
//        System.out.println(h1.buildHouse());
//        System.out.println(Arrays.toString(built));
//        System.out.println(h2.buildHouse());
//        System.out.println(Arrays.toString(built));
//        System.out.println(h3.buildHouse());
//        System.out.println(Arrays.toString(built));
    }
}
