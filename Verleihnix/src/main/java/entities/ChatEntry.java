package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name="ChatEntry")
public class ChatEntry{

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonIgnore
    @NotNull
    private InsertionRequest insertionRequest;

    @Column
    @NotNull
    private long senderId;

    @Column
    private String message;

    @Column
    private long sendDate;

    @Column
    private boolean readByListener;

    public ChatEntry() {
        super();
        this.readByListener = false;
        this.sendDate = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InsertionRequest getInsertionRequest() {
        return insertionRequest;
    }

    public void setInsertionRequest(InsertionRequest insertionRequest) {
        this.insertionRequest = insertionRequest;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isReadByListener() {
        return readByListener;
    }

    public void setReadByListener(boolean readByListener) {
        this.readByListener = readByListener;
    }
}
