package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * represents the entity model for an InsertionRequest
 */
@Entity(name="InsertionRequest")
public class InsertionRequest {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonIgnore
    private Insertion insertion;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column
    private Date dateFrom;

    @Column
    private Date dateTo;

    @Column
    private long editAt;

    @JsonIgnore
    private long requesterId;

    @OneToMany(mappedBy = "insertionRequest",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChatEntry> chatEntries = new ArrayList<ChatEntry>();

    public InsertionRequest() {
        super();
    }

    public InsertionRequest(Insertion insertion, State state, Date dateFrom, Date dateTo, long editAt, long requesterId) {
        super();
        this.insertion = insertion;
        this.state = state;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.editAt = editAt;
        this.requesterId = requesterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Insertion getInsertion() {
        return insertion;
    }

    public void setInsertion(Insertion insertion) {
        this.insertion = insertion;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public long getEditAt() {
        return editAt;
    }

    public void setEditAt(long editAt) {
        this.editAt = editAt;
    }

    public long getRequester() {
        return requesterId;
    }

    public void setRequester(long requesterId) {
        this.requesterId = requesterId;
    }

}
