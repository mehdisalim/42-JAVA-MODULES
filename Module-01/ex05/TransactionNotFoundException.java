public class TransactionNotFoundException extends RuntimeException {
    
    final String transactionId;

    public TransactionNotFoundException() {
        this.transactionId = "";
    }

    public TransactionNotFoundException(final String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String getMessage() {
        return "There no transaction associated with this id.";
    }

}
