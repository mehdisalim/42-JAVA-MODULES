public class Program {
    

    public static void main(String[] args) {
        final UsersList usersList = new UsersArrayList();
        for (int i = 0; i < 154; i++) {
            final User user = new User();
            user.setName("user" + i);
            user.setBalance(12 + i);
            usersList.addUser(user);
        }

        // for (int i = 0; i < usersList.size(); i++) {
        //     System.out.println(usersList.getUser(i));
        // }

        System.out.println();
        System.out.println();
        System.out.println("------------------------------");
        System.out.println();
        System.out.println();
        System.out.println(usersList.getUserById(36));
        
    }
}