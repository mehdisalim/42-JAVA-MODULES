import java.util.UUID;

public class Program {
    
    public static void main(String[] args) {
        final User user1 = new User();
        user1.setName("mehdi");
        user1.setBalance(21);
        System.out.println("Balance: " + user1.getBalance());
        user1.setBalance(-1);
        System.out.println(user1);
        
        final Transaction trans1 = new Transaction();
        trans1.setId(UUID.randomUUID());
        trans1.setRecipient(user1);
        trans1.setSender(user1);
        trans1.setTransactionAmount(45);
        trans1.setTransactionCategory(Transaction.TransactionCategory.CREDIT);

        System.out.println(trans1);
        trans1.setTransactionAmount(-14);
        System.out.println(trans1.getTransactionAmount());
        trans1.setTransactionAmount(14);
        System.out.println(trans1.getTransactionAmount());
        
    }
}