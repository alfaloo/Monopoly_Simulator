import java.util.*;
public class Board {
    public static final int JAIL = 10;
    private List<Square> board = new ArrayList<>();
    public Board() {
        // Create all the properties.
        Square p0 = Square.of(Pair.of("property", 0));
        Square p1 = Square.of(Pair.of("property", 1));
        Square p2 = Square.of(Pair.of("property", 2));
        Square p3 = Square.of(Pair.of("property", 3));
        Square p4 = Square.of(Pair.of("property", 4));
        Square p5 = Square.of(Pair.of("property", 5));
        Square p6 = Square.of(Pair.of("property", 6));
        Square p7 = Square.of(Pair.of("property", 7));
        Square p8 = Square.of(Pair.of("property", 8));
        Square p9 = Square.of(Pair.of("property", 9));
        Square p10 = Square.of(Pair.of("property", 10));
        Square p11 = Square.of(Pair.of("property", 11));
        Square p12 = Square.of(Pair.of("property", 12));
        Square p13 = Square.of(Pair.of("property", 13));
        Square p14 = Square.of(Pair.of("property", 14));
        Square p15 = Square.of(Pair.of("property", 15));
        Square p16 = Square.of(Pair.of("property", 16));
        Square p17 = Square.of(Pair.of("property", 17));
        Square p18 = Square.of(Pair.of("property", 18));
        Square p19 = Square.of(Pair.of("property", 19));
        Square p20 = Square.of(Pair.of("property", 20));
        Square p21 = Square.of(Pair.of("property", 21));

        // Create all the trains.
        Square t0 = Square.of(Pair.of("train", 0));
        Square t1 = Square.of(Pair.of("train", 1));
        Square t2 = Square.of(Pair.of("train", 2));
        Square t3 = Square.of(Pair.of("train", 3));

        // Create all the utilities.
        Square u0 = Square.of(Pair.of("utility", 0));
        Square u1 = Square.of(Pair.of("utility", 1));

        // Create all the miscs.
        Square m0 = Square.of(Pair.of("misc", 0));
        Square m1 = Square.of(Pair.of("misc", 1));
        Square m2 = Square.of(Pair.of("misc", 2));
        Square m3 = Square.of(Pair.of("misc", 3));
        Square m4 = Square.of(Pair.of("misc", 4));
        Square m5 = Square.of(Pair.of("misc", 5));
        Square m6 = Square.of(Pair.of("misc", 6));
        Square m7 = Square.of(Pair.of("misc", 7));
        Square m8 = Square.of(Pair.of("misc", 8));
        Square m9 = Square.of(Pair.of("misc", 9));
        Square m10 = Square.of(Pair.of("misc", 10));
        Square m11 = Square.of(Pair.of("misc", 11));

        this.board.add(m0);
        this.board.add(p0);
        this.board.add(m1);
        this.board.add(p1);
        this.board.add(m2);
        this.board.add(t0);
        this.board.add(p2);
        this.board.add(m3);
        this.board.add(p3);
        this.board.add(p4);

        this.board.add(m4);
        this.board.add(p5);
        this.board.add(u0);
        this.board.add(p6);
        this.board.add(p7);
        this.board.add(t1);
        this.board.add(p8);
        this.board.add(m5);
        this.board.add(p9);
        this.board.add(p10);

        this.board.add(m6);
        this.board.add(p11);
        this.board.add(m7);
        this.board.add(p12);
        this.board.add(p13);
        this.board.add(t2);
        this.board.add(p14);
        this.board.add(p15);
        this.board.add(u1);
        this.board.add(p16);

        this.board.add(m8);
        this.board.add(p17);
        this.board.add(p18);
        this.board.add(m9);
        this.board.add(p19);
        this.board.add(t3);
        this.board.add(m10);
        this.board.add(p20);
        this.board.add(m11);
        this.board.add(p21);
    }

    public Square getSquare(int pos) {
        return this.board.get(pos);
    }

    /**
     * Checks if every Square on the board
     * (other than Misc Squares) has an owner.
     *
     * @return True if every Square on the board is owned.
     */
    public boolean complete() {
        for (Square s : this.board) {
            if (s instanceof Misc) {
                continue;
            } else if (s.getOwner() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Assign all unowned Squares to a given account, then build
     * a given amount of houses on each Property.
     *
     * @param account Account to whom unowned Squares will be assigned.
     * @param house   Number of houses to be built on each property.
     */
    public boolean fillRest(Account account, int house) {
        boolean progress = true;

        // Purchase all unowned Squares.
        for (Square s : board) {
            if (s.getOwner() == null) {
                progress = progress && s.purchase(account);
            }
        }

        // Build houses on properties.
        for (int i = 0; i < house; i++) {
            for (Square s : board) {
                if (s instanceof Property && s.getOwner() == account) {
                    Property property = (Property) s;
                    progress = progress && property.buildHouse(account);
                }
            }
        }

        return progress;
    }

    /**
     * Constructs a given number of houses on all the properties
     * owned by a given account.
     *
     * @param account Account whose properties will have houses built.
     * @param house   Number of houses to be built on each property.
     * @return True if successful.
     */
    public boolean buildAll(Account account, int house) {
        boolean progress = true;

        for (int i = 0; i < house; i++) {
            for (Square s : board) {
                if (s instanceof Property) {
                    Property property = (Property) s;
                    Account owner = property.getOwner();
                    if (account.equals(owner)) {
                        progress = progress && property.buildHouse(owner);
                    }
                }
            }
        }

        return progress;
    }

    /**
     * Displays into the console the current state of the board.
     * This includes the owner of each Square and details of each Property.
     */
    public void debug() {
        Property p = (Property) this.getSquare(1);
        Train t = (Train) this.getSquare(5);
        Utility u = (Utility) this.getSquare(12);
        Misc m = (Misc) this.getSquare(0);

        p.debug();
        t.debug();
        u.debug();
        m.debug();
    }
}
