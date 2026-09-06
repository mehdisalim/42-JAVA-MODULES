package fr.fortytwo.chat.models;

import java.util.List;

public class Chatroom {

    private Long            id;
    private String          name;
    private User            owner;
    private List<Message>   messages;


    public Chatroom() {}

    public Chatroom(final Long id,
                    final String name,
                    final User owner,
                    final List<Message> messages) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    // setters
    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    // getters
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public User getOwner() {
        return this.owner;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    // toString
    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator=" + owner +
                ", messages=" + messages +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Chatroom chatroom = (Chatroom) o;

        if (!id.equals(chatroom.id)) return false;
        if (!name.equals(chatroom.name)) return false;
        if (!owner.equals(chatroom.owner)) return false;
        return messages.equals(chatroom.messages);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + messages.hashCode();
        return result;
    }
    
}
