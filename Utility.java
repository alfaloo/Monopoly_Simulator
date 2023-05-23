import java.util.Arrays;

public class Utility extends Square{
    private static final int cost = 150;
    private static final int[] multiplier = new int[] {0, 4, 10};
    private static Account[] owned = new Account[2];
    private int id;

    public Utility(int id) {
        this.id = id;
    }

    @Override
    public boolean purchase(Account account) {
        // Checks for illegal purchase.
        if (this.owned[this.id] != null) {
            System.out.println(String.format("Unable to purchase Utility %d: Already owned by %s", this.id, this.owned[this.id]));
            return false;
        } else if (!account.deduct(this.cost)) {
            System.out.println(String.format("Unable to purchase Utility %d: Insufficient funds by %s", this.id, account));
            return false;
        }

        this.owned[this.id] = account;
        return true;
    }

    @Override
    public Account getOwner() {
        return this.owned[this.id];
    }

    /**
     * For a given account, determine the number of
     * Utilities that said account owns.
     *
     * @param account Account whose ownership is to be checked.
     * @return Number of Utilities that said Account owns.
     */
    public int amountOwned(Account account) {
        int counter = 0;
        for (Account a : this.owned) {
            if (account.equals(a)) counter++;
        }
        return counter;
    }

    @Override
    public boolean visit(Account account, int jumps) {
        Account owner = this.owned[this.id];
        int amount = this.multiplier[this.amountOwned(owner)] * jumps;
        return account.pay(amount, owner);
    }

    @Override
    public void debug() {
        System.out.println("Debugging Utility:");
        System.out.println("owned: " + Arrays.toString(this.owned));
        System.out.println("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof Utility) {
            Utility utility = (Utility) obj;
            // Could implement in the future
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "Utility " + this.id;
    }
}
