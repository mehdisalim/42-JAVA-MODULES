import java.util.UUID;
import java.util.Scanner;

public class Menu {

    private final TransactionsService transactionsService;
    private final boolean isDevMode;

    final private String[] ordersMessage = {
        /* 1 */ "Enter a user name and a balance",
        /* 2 */ "Enter a user ID",
        /* 3 */ "Enter a sender ID, a recipient ID, and a transfer amount",
        /* 4 */ "Enter a user ID",
        /* 5 */ "Enter a user ID and a transfer ID",
        /* 6 */ "Check results:",
        /* 7 */ ""
    };

    public Menu() {
        transactionsService = new TransactionsService();
        this.isDevMode = true;
    }

    public Menu(final boolean isDevMode) {
        transactionsService = new TransactionsService();
        this.isDevMode = isDevMode;
    }

    public void displayMenu() {
        final String[] devItems = {
            "Add a user",
            "View user balances",
            "Perform a transfer",
            "View all transactions for a specific user",
            "DEV - remove a transfer by ID",
            "DEV - check transfer validity",
            "Finish execution",
        };
        final String[] prodItems = {
            "Add a user",
            "View user balances",
            "Perform a transfer",
            "View all transactions for a specific user",
            "Finish execution"
        };
        final String[] list = isDevMode ? devItems : prodItems;
        for (int i = 0; i < list.length; i++) {
            System.out.println("   " + (i + 1) + ". " + list[i]);
        }
    }

    public void diplayOrderMessage(int orderNumber) {
        if (orderNumber < 1 || orderNumber > 7) {
            System.err.println("Illegal Argument");
            System.exit(-1);
        }
        if (!isDevMode && orderNumber == 5) {
            orderNumber = 7;
        }
        System.out.println("   " + ordersMessage[orderNumber - 1]);
    }

    public void printTheResult(final int orderNumber, final Scanner scan) {
        switch (orderNumber) {
            case 1:
                // 1. Add a user.
                addUser(scan);
                break;
            case 2:
                // 2. View user balance.
                showUserBalance(scan);
                break;
            case 3:
                // 3. perform a transfer.
                performTransfer(scan);
                
                break;
            case 4:
                // 4. View all transactions for a specific user.
                showUserTransactions(scan);
                break;
            case 5:
                // 5. Dev - remove a transfer by id.
                if (!isDevMode) {
                    System.exit(0);
                }
                removeTransactionFromUserTransactionsList(scan);
                break;
            case 6:
                // 6. Dev - check transfer validity.
                
                checkTransferValidity();
                break;
            default:
                System.exit(0);
                break;
        }
    }

    private void addUser(final Scanner scan) {
        String name = scan.next();
        int balance = scan.nextInt();

        final User user = new User();
        user.setName(name);
        user.setBalance(balance);

        transactionsService.addUser(user);

    }

    private void showUserBalance(final Scanner scan) {
        final int userId = scan.nextInt();
        final User user = transactionsService.getUser(userId);
        System.out.println("   " + user.getName() + " - " + user.getBalance());
    }

    private void performTransfer(final Scanner scan) {
     final int senderId = scan.nextInt();   
     final int recipientId = scan.nextInt();
     final int amount = scan.nextInt();
     
     transactionsService.transfer(recipientId, senderId, amount);
     System.out.println("   The transfer is completed");
    }

    private void showUserTransactions(final Scanner scan) {
        final int userId = scan.nextInt();

        final Transaction[] trans = transactionsService.getTransactions(userId);
        for (Transaction transaction : trans) {
            if (transaction.getSender().getId() == userId) {
                final User user = transaction.getSender();
                System.out.println("   To " + user.getName() + "(id = " + user.getId() + ") " + transaction.getTransactionAmount() + " with id = " + transaction.getId());
            } else {
                final User user = transaction.getRecipient();
                System.out.println("   From " + user.getName() + "(id = " + user.getId() + ") " + transaction.getTransactionAmount() + " with id = " + transaction.getId());
            }
        }
    }


    private void removeTransactionFromUserTransactionsList(final Scanner scan) {
        final int userId = scan.nextInt();
        final String transactionId = scan.next();

        final Transaction trans = transactionsService.deleteTransaction(UUID.fromString(transactionId), userId);
        User user = trans.getSender().getId() == userId ? trans.getSender() : trans.getRecipient();
        System.out.println("   Transfer To " + user.getName() + "(id = " + user.getId() + ") " + trans.getTransactionAmount() + " removed");
    }


    private void checkTransferValidity() {
        final Transaction[] unvalideTransaction = transactionsService.getUnPairedTransaction();
        for (Transaction transaction : unvalideTransaction) {
            final User sender = transaction.getSender();
            final User recipient = transaction.getRecipient();
            System.out.printf("%s(id = %s) has an unacknowledged transfer id = %s from %s(id = %s) for %s\n", 
                                    sender.getName(),
                                    sender.getId(), 
                                    transaction.getId(), 
                                    recipient.getName(), 
                                    recipient.getId(), 
                                    transaction.getTransactionAmount()
                                );
        }
    }

}