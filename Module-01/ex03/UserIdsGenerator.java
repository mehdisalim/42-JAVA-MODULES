public class UserIdsGenerator {

    private static int currentId;

    private static UserIdsGenerator INSTANCE = null;

    private UserIdsGenerator() {
        currentId = 0;
    }

    public static UserIdsGenerator getInstance() {
        if (INSTANCE == null){
            INSTANCE = new UserIdsGenerator();
        }
        return INSTANCE;
    }


    public int generateId() {
        return ++currentId;
    }

}