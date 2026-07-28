import java.util.UUID;

public class TransactionsService {
    
    private UsersList users;

    public TransactionsService() {
        users = new UsersArrayList();
    }

    // Adding User.
    public void addUser(final User user) {
        users.addUser(user);
        System.out.println("   User with id = " + user.getId() + " is added");
    }

    // Recieve User's Balance.
    public int getUserBalance(final int userId) {
        return users.getUserById(userId).getBalance();
    }

    public User getUser(final int userId) {
        return users.getUserById(userId);
    }

    /**
     * 
     * Performing a transfer transaction (user IDs and transfer amount are specified).
     * In this case, two transactions of DEBIT/CREDIT types are created and added to 
     * recipient and sender. IDs of both transactions must be equal
     * 
     */
    public void transfer(final int recipientId, final int senderId , final int transferAmount) throws IllegalTransactionException {
        if (getUserBalance(senderId) < transferAmount) {
            throw new IllegalTransactionException();
        }

        final User sender = users.getUserById(senderId);
        final User recipient = users.getUserById(recipientId);
        
        final UUID transactionId = UUID.randomUUID();

        final Transaction creditTransaction = new Transaction();
        creditTransaction.setId(transactionId);
        creditTransaction.setRecipient(recipient);
        creditTransaction.setSender(sender);
        creditTransaction.setTransactionCategory(Transaction.TransactionCategory.CREDIT);
        creditTransaction.setTransactionAmount(transferAmount);
  
        final Transaction debitTransaction = new Transaction();
        debitTransaction.setId(transactionId);
        debitTransaction.setRecipient(recipient);
        debitTransaction.setSender(sender);
        debitTransaction.setTransactionCategory(Transaction.TransactionCategory.DEBIT);
        debitTransaction.setTransactionAmount(-transferAmount);

        /// @TODO: I stopped here !!!!
        sender.setBalance(sender.getBalance() - transferAmount);
        recipient.setBalance(recipient.getBalance() + transferAmount);

        sender.addTransaction(creditTransaction);
        recipient.addTransaction(creditTransaction);
    }

    // Retrieving transfers of a specific user (an ARRAY of transfers is returned).
    public Transaction[] getTransactions(final int userId) {
        return users.getUserById(userId).getTransactions().toArray();
    }
    
    // Removing a transaction by ID for a specific user (transaction ID and user ID are specified)
    public Transaction deleteTransaction(final UUID transactionId, final int targetedUserId) {
        final Transaction deletedTransaction = users.getUserById(targetedUserId)
            .getTransactions()
            .deleteTransactionById(transactionId);
        System.out.println("deleting transaction with id : " + transactionId + " and user id : " + targetedUserId);
        return deletedTransaction;
    }

    // Check validity of transactions (returns an ARRAY of unpaired transactions).
    public Transaction[] getUnPairedTransaction() {
        final TransactionsList unpairedTransactions = new TransactionsLinkedList();
        for (int i = 0; i < users.size(); i++) {
            final User user = users.getUser(i);
            final TransactionsList userTransactions = user.getTransactions();
            for (Transaction tran : userTransactions.toArray()) {
                User user2 = tran.getRecipient();
                if (user2.getId() == user.getId())
                    user2 = tran.getSender();
                final Transaction foundedTransaction = user2.getTransactions().getTransactionById(tran.getId());
                if (foundedTransaction == null) {
                    unpairedTransactions.addTransaction(tran);
                }
            }
        }
        return unpairedTransactions.toArray();
    }



}
