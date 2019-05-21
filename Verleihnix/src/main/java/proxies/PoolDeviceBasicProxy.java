package proxies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

@JsonRootName("Pool")
public class PoolDeviceBasicProxy {

    @JsonProperty
    @NotNull
    private long idPool;

    public PoolDeviceBasicProxy() {
        super();
        this.idPool = -1;
    }

    public PoolDeviceBasicProxy(long idPool) {
        this.idPool = idPool;
    }

    public long getPoolId() {
        return idPool;
    }

    public void setPoolId(long idPool) {
        this.idPool = idPool;
    }
}
