package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity(name="InsertionImage")
public class InsertionImage {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonIgnore
    private Insertion insertion;

    @Lob
    @Column
    private StringBuilder image;

    public InsertionImage() {
        super();
    }

    public InsertionImage(StringBuilder image, Insertion insertion) {
        super();
        this.insertion = insertion;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StringBuilder getImage() {
        return image;
    }

    public void setImage(StringBuilder image) {
        this.image = image;
    }

    public Insertion getInsertion() {
        return insertion;
    }

    public void setInsertion(Insertion insertion) {
        this.insertion = insertion;
    }
}
