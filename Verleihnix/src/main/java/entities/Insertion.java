package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Insertion")
public class Insertion {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonIgnore
    private Pool pool;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Lob
    @Column
    private StringBuilder image;

    @Column
    private boolean active;

    @ManyToOne
    @JsonIgnore
    private Product product;

    @OneToMany(mappedBy = "insertion", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<InsertionRequest> insertionRequests = new ArrayList<>();

    public Insertion() {
        super();
    }

    public Insertion(Pool pool,String title, String description, boolean active, Product product) {
        super();
        this.pool = pool;
        this.title = title;
        this.description = description;
        this.active = active;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StringBuilder getImage() {
        return image;
    }

    public void setImage(StringBuilder image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<InsertionRequest> getInsertionRequests() {
        return insertionRequests;
    }

    public void setInsertionRequests(List<InsertionRequest> insertionRequests) {
        this.insertionRequests = insertionRequests;
    }
}
