public class Player extends Account{
    public Player(int id, int amount, Board board) {
        super(id, amount, board);
    }

    @Override
    public boolean setBalance(int amount) {
        // Player instances can not have negative balance.
        if (amount < 0) {
            System.out.println("Invalid balance amount");
            return false;
        }

        super.balance = amount;
        return true;
    }

    @Override
    public boolean receive(int amount) {
        // Player instances can not have negative balance.
        if (amount < 0) {
            return this.deduct(-amount);
        }

        super.balance += amount;
        return true;
    }

    @Override
    public boolean deduct(int amount) {
        // Player instances can not have negative balance.
        if (super.balance < amount) {
            return false;
        }

        super.balance -= amount;
        return true;
    }

    @Override
    public boolean pay(int amount, Account account) {
        // Payment to oneself can be skipped.
        if (account == this) return true;

        // Player instances can not have negative balance.
        if (!this.deduct(amount)) {
            return false;
        }

        account.receive(amount);
        return true;
    }

    /**
     * Retrieves the Square on the board at a given index
     * and this player purchases said Square.
     *
     * @param i Index of the Square on the board which player is to purchase.
     * @return True if successful.
     */
    public boolean purchase(int i) {
        return this.board.getSquare(i).purchase(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof Player) {
            Player player = (Player) obj;
            // Could implement in the future
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "Player " + this.id;
    }

    public static void main(String[] args) {
//        Account p1 = new Player(1, null);
//        Account p2 = new Player(2, null);
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p1.equals(p2));
//        System.out.println(p1.equals(p1));
//        p1.deduct(100);
//        p1.receive(50);
//        p2.pay(100, p1);
//        System.out.println(p1.getBalance());
//        System.out.println(p2.getBalance());
    }
}
