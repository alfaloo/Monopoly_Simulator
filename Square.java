public abstract class Square {
    public static Square of(Pair<String, Integer> idPair) {
        if (idPair.head() == "property") {
            return new Property(idPair.tail());
        } else if (idPair.head() == "train") {
            return new Train(idPair.tail());
        } else if (idPair.head() == "utility") {
            return new Utility(idPair.tail());
        } else if (idPair.head() == "misc") {
            return new Misc(idPair.tail());
        } else return null;
    }

    /**
     * Square is purchased by a given account.
     *
     * @param account Account that purchases the Square.
     * @return True if successful.
     */
    public abstract boolean purchase(Account account);

    /**
     * Getter for the owner of the square.
     *
     * @return Owner Account of the Square.
     */
    public abstract Account getOwner();

    /**
     * Square is visited by a given account by moving
     * a given number of jumps.
     *
     * @param account Account that visits the square.
     * @param jumps   Number of jumps it took to arrive at the Square.
     * @return True if successful.
     */
    public abstract boolean visit(Account account, int jumps);

    /**
     * Displays into the console the current state of the board.
     * This includes the owner of each Square and details of each Property.
     */
    public abstract void debug();
}
