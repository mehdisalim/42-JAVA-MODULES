package fr.fortytwo.sockets.models;

import java.sql.Timestamp;

public class Message {
    private Long id;
    private Long authorId;
    private Long roomId;
    private String text;
    private Timestamp timestamp;

    public Message() {}

    public Message(Long id, Long authorId, Long roomId, String text, Timestamp timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.roomId = roomId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
