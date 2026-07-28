import java.util.UUID;

public interface TransactionsList {

	Transaction		getTransactionById(final UUID transactionId);

	void			addTransaction(final Transaction transaction);

	Transaction		deleteTransactionById(final UUID id);

	Transaction[]	toArray();
}
