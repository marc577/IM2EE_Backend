package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Pool")
public class Pool {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 64)
    private String description;

    @ManyToOne
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "pool", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeviceBasic> basicDevices = new ArrayList<DeviceBasic>();


    public Pool() {
        super();
    }

    public Pool(String description, User user) {
        super();
        this.description = description;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DeviceBasic> getBasicDevices() {
        return basicDevices;
    }

    public void setBasicDevices(List<DeviceBasic> basicDevices) {
        this.basicDevices = basicDevices;
    }
}
