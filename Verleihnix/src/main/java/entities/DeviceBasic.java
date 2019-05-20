package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="DeviceBasic")
public class DeviceBasic {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Pool pool;

    @Column(length = 64)
    private String description;

    //#TODO imageField

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceBasic")
    private List<DeviceElement> deviceElements = new ArrayList<DeviceElement>();

    public DeviceBasic() {
        super();
    }

    public DeviceBasic(String description, Pool pool) {
        super();
        this.description = description;
        this.pool = pool;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DeviceElement> getDeviceElements() {
        return deviceElements;
    }

    public void setDeviceElements(List<DeviceElement> deviceElements) {
        this.deviceElements = deviceElements;
    }


}
