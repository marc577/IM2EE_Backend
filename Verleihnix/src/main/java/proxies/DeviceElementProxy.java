package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

@JsonRootName("DeviceElement")
public class DeviceElementProxy {

    @JsonProperty
    private long id;

    @JsonProperty
    @NotNull
    private long deviceBasicId;

    @JsonProperty
    @NotNull
    private boolean active;

    public DeviceElementProxy() {
        super();
        this.id = -1;
        this.deviceBasicId = -1;
        this.active = false;
    }

    public DeviceElementProxy(long id, long deviceBasicId, boolean active) {
        super();
        this.id = id;
        this.deviceBasicId = deviceBasicId;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceBasicId() {
        return deviceBasicId;
    }

    public void setDeviceBasicId(long deviceBasicId) {
        this.deviceBasicId = deviceBasicId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
