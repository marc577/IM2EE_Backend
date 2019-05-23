package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import entities.Insertion;

import javax.validation.constraints.NotNull;

@JsonRootName("Insertion")
public class InsertionProxy {

    @JsonProperty
    private long id;

    @JsonProperty
    @NotNull
    private long poolId;

    @JsonProperty
    private long productId;

    @JsonProperty
    private String insertionTitle;

    @JsonProperty
    private String insertionDescription;

    @JsonProperty
    private String productTitle;

    @JsonProperty
    private String productDescription;

    @JsonProperty
    private StringBuilder image;

    @JsonProperty
    private boolean active;

    public InsertionProxy() {
        super();
        this.id = -1;
        this.poolId = -1;
        this.productId = -1;
        this.insertionTitle = null;
        this.insertionDescription = "";
        this.productTitle = null;
        this.productDescription = "";
        this.image = new StringBuilder();
        this.active = true;
    }

    public InsertionProxy(long id, long poolId, long productId, String insertionTitle, String insertionDescription, String productTitle, String productDescription, StringBuilder image, boolean active) {
        super();
        this.id = id;
        this.poolId = poolId;
        this.productId = productId;
        this.insertionTitle = insertionTitle;
        this.insertionDescription = insertionDescription;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.image = image;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPoolId() {
        return poolId;
    }

    public void setPoolId(long poolId) {
        this.poolId = poolId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getInsertionTitle() {
        return insertionTitle;
    }

    public void setInsertionTitle(String insertionTitle) {
        this.insertionTitle = insertionTitle;
    }

    public String getInsertionDescription() {
        return insertionDescription;
    }

    public void setInsertionDescription(String insertionDescription) {
        this.insertionDescription = insertionDescription;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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
}
