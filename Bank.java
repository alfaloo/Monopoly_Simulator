public class Bank extends Account{
    public Bank(int id, Board board) {
        super(id, 0, board);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else if (obj instanceof Bank) {
            Bank bank = (Bank) obj;
            // Could implement in the future
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "Bank " + this.id;
    }

    public static void main(String[] args) {
//        Account p1 = new Bank(1, null);
//        Account p2 = new Bank(2, null);
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p1.equals(p2));
//        System.out.println(p1.equals(p1));
//        p1.deduct(2000);
//        p1.receive(50);
//        p2.pay(100, p1);
//        System.out.println(p1.getBalance());
//        System.out.println(p2.getBalance());
    }
}
