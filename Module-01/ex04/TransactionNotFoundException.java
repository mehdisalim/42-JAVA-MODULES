public class TransactionNotFoundException extends RuntimeException {
    
    @Override
    public String getMessage() {
        return "There no transaction associated with this id.";
    }

}
