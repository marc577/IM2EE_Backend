package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("InsertionRequestStateProxy")
public class InsertionRequestStateProxy {

	@JsonProperty
	private String message;

	@JsonProperty
	private int state;

	public InsertionRequestStateProxy() {
		super();
		this.state = -1;
		this.message = "";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
