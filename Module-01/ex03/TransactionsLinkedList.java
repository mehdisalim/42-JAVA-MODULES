import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {

    int length;
    TransactionNode head;
    TransactionNode tail;


    public TransactionsLinkedList() {
        length = 0;
        head = null;
        tail = null;
    }




    public Transaction getTransactionById(final UUID transactionId) throws TransactionNotFoundException {
        TransactionNode tmp = tail;
        while (tmp != null) {
            final Transaction trans = tmp.transaction;
            if (trans.getId() == transactionId)
                return trans; 
            tmp = tmp.next;
        }
        return null;
    }

    @Override
    public void addTransaction(final Transaction transaction) {
        if (tail == null) {
            tail = new TransactionNode(transaction);
            head = tail;
        } else {
            head.next = new TransactionNode(transaction, head);
            head = head.next;
        }
        length++;
    }

    @Override
    public Transaction deleteTransactionById(final UUID id) {
        TransactionNode tmpNode = tail;
        while (tmpNode != null) {
            if (tmpNode.transaction.getId().equals(id)) {
                final Transaction trans = tmpNode.transaction;
                if (tmpNode.previous != null) {
                    tmpNode.previous.next = tmpNode.next;
                    if (tmpNode.next != null) {
                        tmpNode.next.previous = tmpNode.previous;
                    }

                }
                length--;
                return trans;
            }
            tmpNode = tmpNode.next;
        }
        throw new TransactionNotFoundException();
    }

    @Override
    public Transaction[] toArray() {
        final Transaction[] transactions = new Transaction[length];
        TransactionNode tmpNode = tail;
        int counter = 0;
        while (tmpNode != null) {
            transactions[counter++] = tmpNode.transaction;
            tmpNode = tmpNode.next;
        }
        return transactions;
    }

    public class TransactionNode {
        Transaction transaction;
        TransactionNode next;
        TransactionNode previous;

        public TransactionNode(final Transaction transaction) {
            this.transaction = transaction;
            this.next = null;
            this.previous = null;
        }

        public TransactionNode(final Transaction transaction, final TransactionNode previous) {
            this.transaction = transaction;
            this.previous = previous;
            this.next = null;
        }

        public TransactionNode(final Transaction trans, final TransactionNode previous, final TransactionNode next) {
            this.transaction = trans;
            this.next = next;
            this.previous = previous;
        }


    }

}
