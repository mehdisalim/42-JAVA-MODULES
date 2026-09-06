package fr.fortytwo.chat.models;

import java.util.List;

public class User {
    
    private Long id;
    private String login;
    private String password;
    private List<Chatroom> myRooms;
    private List<Chatroom> sharedRooms;

    public User() {}


    public User(final Long id,
        final String login,
        final String password,
        final List<Chatroom> myRooms,
        final List<Chatroom> sharedRooms
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.myRooms = myRooms;
        this.sharedRooms = sharedRooms;
    }

    // setters
    public void setId(final Long id) {
        this.id = id;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setMyRooms(final List<Chatroom> myRooms) {
        this.myRooms = myRooms;
    }

    public void setSharedRooms(final List<Chatroom> sharedRooms) {
        this.sharedRooms = sharedRooms;
    }

    // getters
    public Long getId() {
        return this.id;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public List<Chatroom> getMyRooms() {
        return this.myRooms;
    }

    public List<Chatroom> getSharedRooms() {
        return this.sharedRooms;
    }

    // toString
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", createdRooms=" + myRooms +
                ", rooms=" + sharedRooms +
                '}';
    }


    // equals and hashCode
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        if (!myRooms.equals(user.myRooms)) return false;
        return sharedRooms.equals(user.sharedRooms);
    }


}
