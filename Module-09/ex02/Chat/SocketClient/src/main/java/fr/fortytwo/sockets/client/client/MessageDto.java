package fr.fortytwo.sockets.client.client;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {
    private String type;     
    private String text;
    private Long fromId;
    private Long roomId;
    private String message;

    public MessageDto() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Long getFromId() { return fromId; }
    public void setFromId(Long fromId) { this.fromId = fromId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
