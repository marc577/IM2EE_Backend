package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name="InsertionStateCalendar")
public class InsertionStateCalendar implements IEntity {

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
    private String commentRequester;

    @Column
    private String commentOwner;

    @Column
    private Date editAt;

    @ManyToOne
    @JsonIgnore
    private User requester;


    public InsertionStateCalendar() {
        super();
    }

    public InsertionStateCalendar(Insertion insertion, State state, Date dateFrom, Date dateTo, String commentRequester, String commentOwner, Date editAt, User requester) {
        super();
        this.insertion = insertion;
        this.state = state;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.commentRequester = commentRequester;
        this.commentOwner = commentOwner;
        this.editAt = editAt;
        this.requester = requester;
    }

    public void deleteCascade() {
        EntityHelper.deleteEntity(this, this.id);
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

    public String getCommentRequester() {
        return commentRequester;
    }

    public void setCommentRequester(String commentRequester) {
        this.commentRequester = commentRequester;
    }

    public String getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(String commentOwner) {
        this.commentOwner = commentOwner;
    }

    public Date getEditAt() {
        return editAt;
    }

    public void setEditAt(Date editAt) {
        this.editAt = editAt;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

}
