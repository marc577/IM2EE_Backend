package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Insertion;

import java.util.ArrayList;
import java.util.List;

public class ProductOutProxy {


    @JsonProperty
    private long id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @JsonProperty
    private double minPricePerDay;

    @JsonProperty
    private List<Insertion> insertions;

    @JsonProperty
    private String image;

    public ProductOutProxy() {
        super();
        this.id = -1;
        this.title = "";
        this.description = "";
        this.minPricePerDay = -1;
        this.insertions = new ArrayList<>();
        this.image = null;
    }

    public List<Insertion> getInsertions() {
        return insertions;
    }

    public void setInsertions(List<Insertion> insertions) {
        this.insertions = insertions;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinPricePerDay() {
        return minPricePerDay;
    }

    public void setMinPricePerDay(double minPricePerDay) {
        this.minPricePerDay = minPricePerDay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
