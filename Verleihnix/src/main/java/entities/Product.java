package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Product")
public class Product {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Insertion>  insertions = new ArrayList<>();

    public Product() {
        super();
    }

    public Product(String titel, String description) {
        super();
        this.title = titel;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String titel) {
        this.title = titel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Insertion> getInsertions() {
        return insertions;
    }

    public void setInsertions(List<Insertion> insertions) {
        this.insertions = insertions;
    }
}
