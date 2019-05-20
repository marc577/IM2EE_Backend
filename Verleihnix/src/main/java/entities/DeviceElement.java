package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="DeviceElement")
public class DeviceElement {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private DeviceBasic deviceBasic;

    @Column
    private int active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deviceElement")
    private List<DeviceStateCalendar> deviceStateCalendars = new ArrayList<DeviceStateCalendar>();

    public DeviceElement() {
        super();
    }

    public DeviceElement(DeviceBasic deviceBasic) {
        super();
        this.deviceBasic=deviceBasic;
        this.active=1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DeviceBasic getDeviceBasic() {
        return deviceBasic;
    }

    public void setDeviceBasic(DeviceBasic deviceBasic) {
        this.deviceBasic = deviceBasic;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<DeviceStateCalendar> getDeviceStateCalendars() {
        return deviceStateCalendars;
    }

    public void setDeviceStateCalendars(List<DeviceStateCalendar> deviceStateCalendars) {
        this.deviceStateCalendars = deviceStateCalendars;
    }
}
