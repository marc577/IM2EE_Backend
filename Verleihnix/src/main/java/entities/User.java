package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;


@Entity(name = "User")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 64)
    private String lastName;

    @Column(length = 64)
    private String firstName;

    @Column(length = 64)
    private String email;

    @Column(length = 256)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Pool> pools = new ArrayList<Pool>();

    public User() {
        super();
    }

    public User(String firstName, String lastName, String email, String password) {

        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.pools = Lists.newArrayList();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Pool> getPools() {
        return pools;
    }

    public void setPools(List<Pool> pools) {
        this.pools = pools;
    }

    public void addBlogPools(Pool pool)
    {
        this.pools.add(pool);
    }
}
