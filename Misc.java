import java.util.*;
public class Misc extends Square{
    private static final int[] cost = new int[] {200, 0, -200, 0, 0, 0, 0, 0, -50, 0, 0, -100};
    private int id;

    public Misc(int id) {
        this.id = id;
    }

    @Override
    public boolean purchase(Account account) {
        // Defaults to True.
        return true;
    }

    @Override
    public Account getOwner() {
        // Can not be owned by an Account.
        return null;
    }

    @Override
    public boolean visit(Account account, int jumps) {
        // Edge case of Go-to-jail Misc Square.
        if (this.id == 8) {
            account.setPosition(Board.JAIL);
        }

        int price = this.cost[this.id];

        if (price >= 0) return account.receive(price);
        else return account.deduct(-price);
    }

    @Override
    public void debug() {
        System.out.println("Debugging Misc:");
        System.out.println("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof Misc) {
            Misc misc = (Misc) obj;
            // Could implement in the future
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "Misc " + this.id;
    }
}
