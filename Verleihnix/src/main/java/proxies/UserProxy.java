package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Pool")
public class UserProxy {

	@JsonProperty
	private long id;

	@JsonProperty
	private String token;

	@JsonProperty
	private String lastName;

	@JsonProperty
	private String firstName;

	@JsonProperty
	private String email;

	public UserProxy(){
		super();
	}
	public UserProxy(long id, String token, String lastName, String firstName, String email){
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.token = token;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
