import java.util.UUID;

public class Transaction {

    private UUID id;
    private User recipient;
    private User sender;
    private TransactionCategory transactionCategory;
    private int transactionAmount;

    enum TransactionCategory {
        CREDIT,
        DEBIT
    }


    public void setId(final UUID id) {
        this.id = id;
    }

    public void setRecipient(final User recipient) {
        this.recipient = recipient;
    }

    public void setSender(final User sender) {
        this.sender = sender;
    }

    public void setTransactionCategory(final TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public void setTransactionAmount(final int transactionAmount) {
        if (this.transactionCategory == TransactionCategory.CREDIT && transactionAmount < 0) {
            System.err.println("The Transaction amount should not be negative in the Credit transaction type.");
            return ;
        }
        if (this.transactionCategory == TransactionCategory.DEBIT && transactionAmount > 0) {
            System.err.println("The Transaction amount should not be positive in the Credit transaction type.");
            return ;
        }
        this.transactionAmount = transactionAmount;
    }


    public UUID getId() {
        return this.id;
    }

    public User getRecipient() {
        return this.recipient;
    }


    public User getSender() {
        return this.sender;
    }

    public TransactionCategory getTransactionCategory() {
        return this.transactionCategory;
    }

    public int getTransactionAmount() {
        return this.transactionAmount;
    }

    @Override
    public String toString() {
        return  "id: " + this.id +
                " | recipient: " + this.recipient +
                " | sender: " + this.sender +
                " | transaction category: " + this.transactionCategory +
                " | transaction amount: " + this.transactionAmount;
    }


}