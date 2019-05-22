package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Pool")
public class Pool {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(length = 256)
    private String description;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "pool",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insertion> insertions = new ArrayList<>();


    public Pool() {
        super();
    }

    public Pool(String description, User user) {
        super();
        this.description = description;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //@OneToMany(mappedBy = "pool",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Insertion> getInsertions() {
        return insertions;
    }

    public void setInsertions(List<Insertion> insertions) {
        this.insertions = insertions;
    }
}
