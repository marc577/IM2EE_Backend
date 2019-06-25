package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import entities.Insertion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * represents the in-/ output data object for the Product
 */
@JsonRootName("Pool")
public class ProductProxy {

	@JsonProperty
	private long id;

	@JsonProperty
	@NotNull
	private String title;

	@JsonProperty
	@NotNull
	private String description;

	@JsonProperty
	private List<Insertion> insertions = new ArrayList<Insertion>();

	@JsonProperty
	private String image;

	public ProductProxy(){
		super();
		this.id = -1;
		this.description = null;
		this.image = null;
	}

	public ProductProxy(long id, String description){
		super();
		this.id = id;
		this.description = description;
		this.image = null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public List<Insertion> getInsertions() {
		return insertions;
	}

	public void setInsertions(List<Insertion> insertions) {
		this.insertions = insertions;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}