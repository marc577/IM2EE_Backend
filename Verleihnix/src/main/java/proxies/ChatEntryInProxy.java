package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

/**
 * represents the in-/ output data object for the ChatEntry
 */
@JsonRootName("ChatEntry")
public class ChatEntryInProxy {

    @JsonProperty
    private String message;

    @JsonProperty
    @NotNull
    private long idSender;

    @JsonProperty
    @NotNull
    private long idInsertionRequest;

    public ChatEntryInProxy() {
        super();
        this.idSender = -1;
        this.message = "";
        this.idInsertionRequest = -1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getIdSender() {
        return idSender;
    }

    /**
     *
     * @param idSender
     */
    public void setIdSender(long idSender) {
        this.idSender = idSender;
    }

    /**
     *
     * @return
     */
    public long getIdInsertionRequest() {
        return idInsertionRequest;
    }

    public void setIdInsertionRequest(long idInsertionRequest) {
        this.idInsertionRequest = idInsertionRequest;
    }
}
