package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import entities.InsertionRequest;
import entities.State;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonRootName("InsertionRequestProxy")
public class InsertionRequestProxy {

    @JsonProperty
    private long id;

    @NotNull
    @JsonProperty
    private Date dateFrom;

    @NotNull
    @JsonProperty
    private Date dateTo;

    @JsonProperty
    private State state;

    @NotNull
    @JsonProperty
    private long insertionId;

    @JsonProperty
    private String insertionTitle;

    @JsonProperty
    private long requesterId;

    @JsonProperty
    private String message;

    @JsonProperty
    private long editAt;

    @JsonProperty
    private int type;

    public InsertionRequestProxy() {
        super();
        this.id = -1;
        this.dateFrom = null;
        this.dateTo = null;
        this.state = null;
        this.insertionId = -1;
        this.requesterId = -1;
        this.message = "";
        this.insertionTitle = "";
        this.type = 1;
    }

    public InsertionRequestProxy(InsertionRequest insertionRequest, int type) {
        this(insertionRequest);
        this.type = type;

    }

    public InsertionRequestProxy(InsertionRequest insertionRequest) {
        super();
        this.id = insertionRequest.getId();
        this.dateFrom = insertionRequest.getDateFrom();
        this.dateTo = insertionRequest.getDateTo();
        this.insertionId = insertionRequest.getInsertion().getId();
        this.insertionTitle = insertionRequest.getInsertion().getTitle();
        this.requesterId = insertionRequest.getRequester();
        this.editAt = insertionRequest.getEditAt();
        this.state = insertionRequest.getState();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getEditAt() {
        return editAt;
    }

    public void setEditAt(long editAt) {
        this.editAt = editAt;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(long requesterId) {
        this.requesterId = requesterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getInsertionId() {
        return insertionId;
    }

    public void setInsertionId(long insertionId) {
        this.insertionId = insertionId;
    }

    public String getInsertionTitle() {
        return insertionTitle;
    }

    public void setInsertionTitle(String insertionTitle) {
        this.insertionTitle = insertionTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
