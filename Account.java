public abstract class Account {
    protected int id;
    protected int balance;
    protected Board board;
    protected int position;

    protected Account(int id, int balance, Board board) {
        this.id = id;
        this.balance = balance;
        this.board = board;
        this.position = 0;
    }

    /**
     * Getter for the balance field.
     *
     * @return Balance of the account.
     */
    public int getBalance() {
        return this.balance;
    }

    /**
     * Setter for the balance field.
     *
     * @param amount New balance of the account.
     * @return True if successful.
     */
    public boolean setBalance(int amount) {
        this.balance = amount;
        return true;
    }

    /**
     * Setter for the position field.
     *
     * @param pos New position of the account.
     * @return True if successful.
     */
    public boolean setPosition(int pos) {
        // Board has maximum position of 39.
        if (pos >= 40) return false;

        this.position = pos;
        return true;
    }

    /**
     * Increases the account's balance by a given amount.
     *
     * @param amount Amount to receive.
     * @return True if successful.
     */
    public boolean receive(int amount) {
        this.balance += amount;
        return true;
    }

    /**
     * Decreases the account's balance by a given amount.
     *
     * @param amount Amount to deduct.
     * @return True if successful.
     */
    public boolean deduct(int amount) {
        this.balance -= amount;
        return true;
    }

    /**
     * Transfers a given amount from this account to the given account.
     *
     * @param amount  Amount to pay.
     * @param account Receiver of the money.
     * @return True if successful.
     */
    public boolean pay(int amount, Account account) {
        this.deduct(amount);
        account.receive(amount);
        return true;
    }

    /**
     * Moves the player to a new position on the board, then
     * executes the logic to visit the board, including any
     * transaction that will happen at the landing Square.
     *
     * @param jumps Number of jumps on the board.
     * @return True if successful.
     */
    public boolean move(int jumps) {
        // Jail from dice roll.
        if (jumps == -1) {
            this.position = Board.JAIL;
            return this.deduct(50);
        }

        this.position += jumps;

        // If player passes start, grant them funds.
        if (this.position >= 40) {
            this.position = this.position % 40;
            return this.receive(200);
        }

        // Visit landing Square.
        Square square = this.board.getSquare(this.position);
        return square.visit(this, jumps);
    }
}
