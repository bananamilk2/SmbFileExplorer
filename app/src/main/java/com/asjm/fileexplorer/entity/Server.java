package com.asjm.fileexplorer.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity(nameInDb = "tableServers")
public class Server implements Serializable {

    private static final long serialVersionUID = 123456789L;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "address")
    private String address;

    @Property(nameInDb = "username")
    private String username;

    @Property(nameInDb = "password")
    private String password;

    @Generated(hash = 57412934)
    public Server(Long id, String name, String address, String username,
            String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    @Generated(hash = 1151933134)
    public Server() {
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
