public class Program {
    
    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            final User user1 = new User();
            user1.setName("user" + i);
            user1.setBalance(12 + i);
            System.out.println(user1);
        }
        
    }
}