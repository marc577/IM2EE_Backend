package proxies;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * represents the in-/ output data object for the Pool
 */
@JsonRootName("Pool")
public class PoolProxy {

	@JsonProperty
	private long id;

	@JsonProperty
	@NotNull
	private String description;

	public PoolProxy(){
		super();
		this.id = -1;
		this.description = null;
	}

	public PoolProxy(long id, String description){
		super();
		this.id = id;
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
}