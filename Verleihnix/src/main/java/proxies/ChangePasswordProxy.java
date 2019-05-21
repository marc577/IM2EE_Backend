package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

@JsonRootName("DeviceBasic")
public class ChangePasswordProxy {

    @JsonProperty
    @NotNull
    private String oldPassword;

    @JsonProperty
    @NotNull
    private String newPassword;

    @JsonProperty
    @NotNull
    private String passwordConfirmation;

    public ChangePasswordProxy() {
        super();
        this.oldPassword=null;
        this.newPassword=null;
        this.passwordConfirmation=null;
    }

    public ChangePasswordProxy(String oldPassword, String newPassword, String passwordConfirmation) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
