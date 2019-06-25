package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

/**
 * represents the in-/ output data object for the password
 */
@JsonRootName("DeviceBasic")
public class ChangePasswordProxy {

    @JsonProperty
    @NotNull
    private String newPassword;

    @JsonProperty
    @NotNull
    private String passwordConfirmation;

    public ChangePasswordProxy() {
        super();
        this.newPassword=null;
        this.passwordConfirmation=null;
    }

    public ChangePasswordProxy(String newPassword, String passwordConfirmation) {
        this.newPassword = newPassword;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
