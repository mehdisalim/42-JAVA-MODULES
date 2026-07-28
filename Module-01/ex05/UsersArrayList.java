public class UsersArrayList implements UsersList {
    

    private User[] users;
    private int size;
    private int length;
    

    public UsersArrayList() {
        users = new User[10];
        size = 0;
        length = 10;
    }

    @Override
    public void addUser(final User user) {
        if (size >= length)
            increaseTheSizeByHalf();
        users[size] = user;
        size++;
    }

    @Override
    public User getUserById(final int id) throws UserNotFoundException {
        for (int i = 0; i < size; i++) {
            if (users[i].getId() == id) {
                return users[i];
            }
        }
        throw new UserNotFoundException();
    }

    @Override
    public User getUser(final int index) throws OutOfBoundException {
        if (index < 0 || index > size)
            throw new OutOfBoundException();
        return users[index];
    }


    @Override
    public int size() {
        return size;
    }
    


    private void increaseTheSizeByHalf() {
        length = length + (length / 2);
        final User[] newUsers = new User[length];
        for (int i = 0; i < size; i++) {
            newUsers[i] = users[i];
        }
        users = newUsers;
    }


}
