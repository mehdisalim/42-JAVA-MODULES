import java.util.UUID;


public class Program {
    

    public static void main(String[] args) {
        final TransactionsList transactions = new TransactionsLinkedList();
        UUID id = null;
        for (int i = 0; i < 10; i++) {
            final User user = new User();
            user.setName("Mehdi");
            user.setBalance(74);

            final Transaction trans = new Transaction();
            trans.setRecipient(user);
            trans.setSender(user);
            trans.setTransactionAmount(895);
            trans.setTransactionCategory(Transaction.TransactionCategory.CREDIT);
            final UUID transId = UUID.randomUUID();
            trans.setId(transId);
            if (i == 5)
                id = transId;
            transactions.addTransaction(trans);
        }

        System.out.println("print All the transactions: ");
        Transaction[] arrayOfTransactions = transactions.toArray();
        for (final Transaction transaction : arrayOfTransactions) {
            System.out.println(transaction);
        }

        System.out.println();
        System.out.println();
        System.out.println("delete The transaction associated with this id: " + id);
        final Transaction deletedTransaction = transactions.deleteTransactionById(id);
        System.out.println("deleted Transaction => " + deletedTransaction);

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("print All the transactions after deletion: ");
        arrayOfTransactions = transactions.toArray();
        for (final Transaction transaction : arrayOfTransactions) {
            System.out.println(transaction);
        }

    }
}