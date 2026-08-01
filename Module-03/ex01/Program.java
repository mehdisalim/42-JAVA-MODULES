public class Program {
    static int count;

    private boolean isEggTurn = true;

    private synchronized void eggPrint() {
        while (!isEggTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Egg");
        isEggTurn = false;
        notifyAll();
    }

    private synchronized void henPrint() {
        while (isEggTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Hen");
        isEggTurn = true;
        notifyAll();
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length < 1 || !args[0].startsWith("--count=")) {
            System.err.println("Error: Invalid Argument !");
            System.exit(-1);
        }
        final String[] counts = args[0].split("=");
        count = Integer.parseInt(counts[1]);
        final Program p = new Program();
        Runnable egg = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    p.eggPrint();
                }
            }
        };

        Runnable hen = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    p.henPrint();
                }

            } 
        };

        Thread eggThread = new Thread(egg);
        Thread henThread = new Thread(hen);

        eggThread.start();
        henThread.start();


        eggThread.join();
        henThread.join();
        
    }

}
