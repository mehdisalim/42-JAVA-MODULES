import java.util.UUID;

public interface TransactionsList {
	
	void			addTransaction(final Transaction transaction);

	Transaction		deleteTransactionById(final UUID id);

	Transaction[]	toArray();
}
