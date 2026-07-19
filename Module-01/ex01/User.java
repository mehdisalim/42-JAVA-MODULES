public class User {

    private int id;
    private String name;
    private int balance;

    public User() {
        this.id = UserIdsGenerator.getInstance().generateId();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setBalance(final int balance) {
        if (balance < 0) {
            System.err.println("User balance cannot be negative !");
            return ;
        }
        this.balance = balance;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }


    @Override
    public String toString() {
        return  "id: " + this.id + 
                " | name: " + this.name + 
                " | balance: " + this.balance;
    }



}
