package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

/**
 * represents the in-/ output data object for the registration
 */
@JsonRootName("Pool")
public class RegisterProxy {

	@JsonProperty
	@NotNull
	private String email;

	@JsonProperty
	private String lastName;

	@JsonProperty
	private String firstName;

	@JsonProperty
	private String password;

	public RegisterProxy(){
		super();
	}
	public RegisterProxy(String lastName, String firstName, String email, String password){
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
