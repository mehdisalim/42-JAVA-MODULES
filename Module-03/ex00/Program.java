public class Program implements Runnable {
    static int count;

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            System.out.println("Human");
        }
    }

    public static void main(String[] args) {
        if (args.length < 1 || !args[0].startsWith("--count=")) {
            System.err.println("Error: Invalid Argument !");
            System.exit(-1);
        }
        final String[] counts = args[0].split("=");
        count = Integer.parseInt(counts[1]);


        Runnable egg = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    System.out.println("Egg");
                }
            }
        };

        

        Thread eggThread = new Thread(egg);
        Thread henThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    System.out.println("Hen");
                }
            }
        });

        Program main = new Program();


        Thread mainThread = new Thread(main);

        eggThread.start();
        henThread.start();
        
        try {
            eggThread.join();
            henThread.join();
            
            mainThread.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        

    }

}
