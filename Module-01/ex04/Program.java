public class Program {
    

    public static void main(String[] args) {

        final TransactionsService transService = new TransactionsService();

        for (int i = 0; i < 4; i++) {
            final User user = new User();

            user.setName("mehdi salim");
            user.setBalance(i * 1240);
            transService.addUser(user);
        }

            transService.transfer(1, 2, 41);
            transService.transfer(1, 2, 41);
            transService.transfer(1, 2, 41);
            transService.transfer(1, 2, 41);
            transService.transfer(3, 2, 41);
            transService.transfer(1, 3, 41);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("displying transactions of the user with id 1");

        final Transaction[] transactions = transService.getTransactions(1);
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        transService.deleteTransaction(transService.getTransactions(1)[0].getId(), 1);
 




        
        final Transaction[] transactions2 = transService.getTransactions(1);
        for (Transaction transaction : transactions2) {
            System.out.println(transaction);
        }
        

        System.out.println("Print unpaired transactions: \n");


        final Transaction[] unpairedTransactions = transService.getUnPairedTransaction();
        for (Transaction transaction : unpairedTransactions) {
            System.out.println(transaction);
        }

    }
}