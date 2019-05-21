package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

@JsonRootName("DeviceBasic")
public class DeviceBasicProxy {

    @JsonProperty
    private long id;

    @JsonProperty
    @NotNull
    private String description;

    @JsonProperty
    @NotNull
    private long idPool;

    public DeviceBasicProxy() {
        super();
        this.id = -1;
        this.description = null;
        this.idPool = -1;
    }

    public DeviceBasicProxy(long id, String description, long idPool) {
        super();
        this.id = id;
        this.description = description;
        this.idPool = idPool;
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

    public long getIdPool() {
        return idPool;
    }

    public void setIdPool(long idPool) {
        this.idPool = idPool;
    }
}
