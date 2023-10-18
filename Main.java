import java.util.*;
public class Main {
    private Board board;
    private Dice dice;

    // Input values by user
    private int inputPlayers;
    private int[][] inputAssets;
    private int[] inputHouses;
    private int[] inputBalance;
    private int inputCycle;
    private int inputIteration;

    // Feeding values from within code
    public Main(long seed) {
        this.board = new Board();
        this.dice = new Dice(seed);
    }

    // Automatically feed values from Scanner
    public Main() {
        this.board = new Board();

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter a seed for the dice:");
        int seed = scan.nextInt();
        this.dice = new Dice(seed);

        System.out.println("Please enter the number of players:");
        this.inputPlayers = scan.nextInt();

        this.inputAssets = new int[inputPlayers][];
        for (int i = 0; i < inputPlayers; i++) {
            System.out.println(String.format("Please enter the number of owned assets of player %d:", i + 1));
            int amountOfOwnedAssets = scan.nextInt();
            this.inputAssets[i] = new int[amountOfOwnedAssets];

            System.out.println(String.format("Please enter the owned assets of player %d:", i + 1));
            for (int j = 0; j < amountOfOwnedAssets; j++) {
                this.inputAssets[i][j] = scan.nextInt();
            }
        }

        this.inputHouses = new int[inputPlayers];
        System.out.println("Please enter the amount of houses owned by each player:");
        for (int i = 0; i < inputPlayers; i++) {
            this.inputHouses[i] = scan.nextInt();
        }

        this.inputBalance = new int[inputPlayers];
        System.out.println("Please enter the balance of each player:");
        for (int i = 0; i < inputPlayers; i++) {
            this.inputBalance[i] = scan.nextInt();
        }

        System.out.println("Please enter the number of cycles to run:");
        this.inputCycle = scan.nextInt();

        System.out.println("Please enter the number of iterations to average:");
        this.inputIteration = scan.nextInt();

        System.out.println("Type 'run' to confirm execution:");
        String confirm = scan.next();
        while (!confirm.equals("run")) {
            if (confirm.equals("exit")) {
                System.exit(0);
            }
            confirm = scan.next();
        }

        this.run(this.inputAssets, this.inputHouses, this.inputBalance, this.inputCycle, this.inputIteration);

    }

    /**
     * Executes the simulation for a given number of cycles
     * and a given number of repetitions. Prints out the average
     * balance, balance change, and the cycle in which a player
     * goes bankrupt, if at all.
     *
     * @param assets  The asset which each player owns.
     * @param houses  The amount of houses each player owns on their properties.
     * @param balance The starting balance of each player.
     * @param cycle   Number of cycles to simulate.
     * @param n       Number of simulation from which an average is calculated.
     * @return True if the simulation successfully completed.
     */
    public boolean run(int[][] assets, int[] houses, int[] balance, int cycle, int n) {
        int numberOfPlayers = assets.length;

        // Checks for invalid inputs.
        if (numberOfPlayers != houses.length) {
            System.out.println("Invalid inputs: Number of players do not match");
            return false;
        } else {
            for (int i : houses) {
                if (i > 5) {
                    System.out.println("Invalid inputs: Number of houses exceeds limit");
                    return false;
                }
            }
        }

        List<Account> accounts = new ArrayList<>();

        // Initialise the board with correct ownership and houses.
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i, 10000, this.board);

            for(int j : assets[i]) {
                player.purchase(j);
            }

            board.buildAll(player, houses[i]);
            player.setBalance(balance[i]);

            accounts.add(player);
        }

        // Assign all unowned buildings to the bank.
        if (!board.complete()) {
            Bank bank = new Bank(0, this.board);
            this.board.fillRest(bank, houses[0]);
            accounts.add(bank);
        }

        // Create a list of lists that will hold each player's data.
        List<List<Pair<Integer, Integer>>> allResult = new ArrayList<>();
        for(int i = 0; i < numberOfPlayers; i++) {
            List<Pair<Integer, Integer>> each = new ArrayList<>();
            allResult.add(each);
        }

        // Repeat the simulation for n number of times.
        for (int repetition = 0; repetition < n; repetition++) {
            // Reset the players' balance before each simulation.
            for (int i = 0; i < numberOfPlayers; i++) {
                accounts.get(i).setBalance(balance[i]);
            }

            // Call the simulate method and distribute the returned data into allResult.
            List<Pair<Integer, Integer>> simResult = simulate(cycle, accounts);
            for (int i = 0; i < numberOfPlayers; i++) {
                allResult.get(i).add(simResult.get(i));
            }
        }

        // Display the data of each player.
        for (int i = 0; i < numberOfPlayers; i++) {
            display(allResult.get(i), accounts.get(i), cycle, n);
        }

        return true;
    }

    /**
     * Executes the simulation for a given number of cycles. Compile the
     * data into a list of Pairs.
     *
     * @param cycle    The asset which each player owns.
     * @param accounts The amount of houses each player owns their properties.
     * @return List of Pairs whose head is the resulting balance
     *         (-1 if player has gone bankrupt), and whose tail is the change
     *         in balance (cycle at which the player went bankrupt). The index
     *         of each Pair corresponds to the player which it refers to.
     */
    public List<Pair<Integer, Integer>> simulate(int cycle, List<Account> accounts) {

        // Creates a map that keeps track of starting balance.
        Map<Account, Integer> initialBalance = new HashMap<>();
        for (Account a : accounts) {
            initialBalance.put(a, a.getBalance());
        }

        // Creates a list and map that keeps track of when a player goes bankrupt.
        List<Account> out = new ArrayList<>();
        Map<Account, Integer> outCycle = new HashMap<>();

        // For each cycle, every player takes turns to move.
        for (int c = 0; c < cycle; c++) {
            for (Account a : accounts) {
                // Skip bankrupt players.
                if(out.contains(a)) continue;

                int jump = this.dice.roll();

                // Keeps track of bankrupt players.
                if (!a.move(jump)) {
                    out.add(a);
                    outCycle.put(a, c);
                }
            }
        }

        // For every player, create a Pair and add it into the result list at the corresponding index.
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for (Account a : accounts) {
            if(out.contains(a)) {
                result.add(Pair.of(-1, outCycle.get(a)));
            } else {
                result.add(Pair.of(a.getBalance(), a.getBalance() - initialBalance.get(a)));
            }
        }

        return result;
    }

    /**
     * Displays into the console the player's data.
     * Data include their average balance, change in balance
     * and the cycle at which they went bankrupt.
     *
     * @param data    Each Pair contains data from one iteration of the simulation.
     * @param account The player/account that the data belongs to.
     * @param cycle   Number of cycles to simulate
     * @param n       Number of simulation from which an average is calculated.
     */
    public void display(List<Pair<Integer, Integer>> data, Account account, int cycle, int n) {
        // Initialising variables.
        int out = 0;
        int survived = 0;
        int outCycle = 0;
        int balance = 0;
        int difference = 0;

        // For each pair, update the variables.
        for (Pair<Integer, Integer> p : data) {
            if (p.head() == -1) {
                out++;
                outCycle += p.tail();
            } else {
                survived++;
                balance += p.head();
                difference += p.tail();
            }
        }

        // Calculate the averages.
        int averageOutCycle = out == 0 ? -1 : outCycle / out;
        int averageBalance = survived == 0 ? -1 : balance / survived;
        int averageDifference = survived == 0 ? -1 : difference / survived;

        // Prints out data in worded format.
        System.out.println(String.format("%s: Simulated %d cycles for %d repetitions", account, cycle, n));
        System.out.println(String.format("Survived %d rounds; Balance of %d; Net balance change of %d", survived, averageBalance, averageDifference));
        System.out.println(String.format("Out %d rounds; Out cycle of %d", out, averageOutCycle));
        System.out.println("\n");
    }

    /**
     * Displays into the console the current state of the board.
     * This includes the owner of each Square and details of each Property.
     */
    public void debug() {
        this.board.debug();
    }

    public static void main(String[] args) {

        int[][] assets = new int[][] {
                {5, 15, 25, 35, 6, 8, 9, 21, 23, 24},
                {1, 3, 11, 13, 14, 16, 18, 19},
                {12, 28, 31, 32, 34},
                {26, 27, 29, 37, 39}
        };
        int[] houses = new int[] {2, 2, 2, 2};
        int[] balance = new int[] {2000, 2000, 2000, 2000};

        Main main = new Main(1);
        main.run(assets, houses, balance, 50, 100);

//        Main main = new Main();

        //main.debug();
    }
}