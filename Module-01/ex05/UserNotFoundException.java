public class UserNotFoundException extends RuntimeException {
    
    @Override
    public String getMessage() {
        return "user not found.";
    }

}
