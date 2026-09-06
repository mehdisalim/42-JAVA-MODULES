package fr.fortytwo.chat.models;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private User author;
    private Chatroom room;
    private String text;
    private LocalDateTime createdAt;


    public Message() {}

    public Message(final Long id,
                   final User author,
                   final Chatroom room,
                   final String text,
                   final LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.createdAt = createdAt;
    }

    // setters
    public void setId(final Long id) {
        this.id = id;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public void setRoom(final Chatroom room) {
        this.room = room;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // getters

    public Long getId() {
        return this.id;
    }

    public User getAuthor() {
        return this.author;
    }

    public Chatroom getRoom() {
        return this.room;
    }

    public String getText() {
        return this.text;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    // toString
    @Override
    public String toString() {
        return "Message{\n" +
                "id=" + id +
                ", \nauthor=" + author +
                ", \nroom=" + room +
                ", \ntext='" + text + '\'' +
                ", \ndateTime=" + createdAt +
                "\n}";
    }

    // equals and hashCode
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Message message = (Message) o;

        if (!id.equals(message.id)) return false;
        if (!author.equals(message.author)) return false;
        if (!room.equals(message.room)) return false;
        if (!text.equals(message.text)) return false;
        return createdAt.equals(message.createdAt);
    }


    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + room.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }


}
