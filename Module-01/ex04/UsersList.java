
public interface UsersList {
    
    void    addUser(final User user);

    User    getUserById(final int id);

    User    getUser(final int index);

    int     size();

}
