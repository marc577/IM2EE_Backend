package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import entities.Insertion;
import entities.Product;

/**
 * represents the output data object for the insertion
 */
@JsonRootName("Insertion")
public class InsertionOutProxy {

    @JsonProperty
    private Insertion insertion;

    @JsonProperty
    private Product product;

    public InsertionOutProxy() {
        super();
        this.insertion=null;
        this.product=null;
    }

    public Insertion getInsertion() {
        return insertion;
    }

    public void setInsertion(Insertion insertion) {
        this.insertion = insertion;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
