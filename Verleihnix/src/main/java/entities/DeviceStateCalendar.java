package entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name="DeviceStateCalendar")
public class DeviceStateCalendar {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column
    private Date date;

    @ManyToOne
    private DeviceElement deviceElement;

    public DeviceStateCalendar() {
        super();
    }

    public DeviceStateCalendar(DeviceElement deviceElement, State state, Date date) {
        super();
        this.deviceElement = deviceElement;
        this.state = state;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DeviceElement getDeviceElement() {
        return deviceElement;
    }

    public void setDeviceElement(DeviceElement deviceElement) {
        this.deviceElement = deviceElement;
    }
}
