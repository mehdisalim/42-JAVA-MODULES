public class Program {
    

    public static void main(String[] args) {

        final TransactionsService transService = new TransactionsService();

        for (int i = 0; i < 10; i++) {
            final User user = new User();

            user.setName("mehdi salim");
            user.setBalance(i * 124);
            transService.addUser(user);
        }

        for (int i = 1; i < 5; i++) {
            transService.transfer(i, 10 - i, i * 41);
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        final Transaction[] transactions = transService.getTransactions(1);
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        transService.deleteTransaction(transactions[0].getId(), 1);
        



        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        
        final Transaction[] transactions2 = transService.getTransactions(1);
        for (Transaction transaction : transactions2) {
            System.out.println(transaction);
        }
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Print unpaired transactions: \n");


        final Transaction[] unpairedTransactions = transService.getUnPairedTransaction();
        for (Transaction transaction : unpairedTransactions) {
            System.out.println(transaction);
        }

    }
}