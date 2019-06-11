package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import entities.Insertion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonRootName("Product")
public class ProductImgProxy {

	@JsonProperty
	private long id;

	@JsonProperty
	@NotNull
	private String title;

	@JsonProperty
	@NotNull
	private String description;

	@JsonProperty
	private double minPricePerDay;

	@JsonProperty
	private List<Insertion> insertions;

	@JsonProperty
	private String image;

	public ProductImgProxy(){
		super();
		this.id = -1;
		this.description = null;
		this.image = null;
	}

	public ProductImgProxy(long id, String title, String description){
		super();
		this.id = id;
		this.title = title;
		this.description = description;
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

	public double getMinPricePerDay() {
		return minPricePerDay;
	}

	public void setMinPricePerDay(double minPricePerDay) {
		this.minPricePerDay = minPricePerDay;
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

