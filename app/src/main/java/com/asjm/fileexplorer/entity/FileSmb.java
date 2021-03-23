package com.asjm.fileexplorer.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

@Entity(nameInDb = "tableFiles")
public class FileSmb {
    @Id(autoincrement = true)
    private Long id;
    @Property
    private int index;
    @Index(unique = true)
    private String hash;
    @Property
    private String fileSource;
    @Property
    private String name;
    @Property
    private Long size;
    @Property
    private Long date;
    @Property
    private Date downloadTime;
    @Property
    private boolean dir;
    @Property
    private int type;
    @Property
    private String path;

    public FileSmb() {
    }

    @Generated(hash = 951967118)
    public FileSmb(Long id, int index, String hash, String fileSource, String name,
                   Long size, Long date, Date downloadTime, boolean dir, int type,
                   String path) {
        this.id = id;
        this.index = index;
        this.hash = hash;
        this.fileSource = fileSource;
        this.name = name;
        this.size = size;
        this.date = date;
        this.downloadTime = downloadTime;
        this.dir = dir;
        this.type = type;
        this.path = path;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileSource() {
        return this.fileSource;
    }

    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getDate() {
        return this.date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Date getDownloadTime() {
        return this.downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    public boolean isDir() {
        return this.dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "FileSmb{" +
                "id=" + id +
                ", index=" + index +
                ", hash='" + hash + '\'' +
                ", fileSource='" + fileSource + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", date=" + date +
                ", downloadTime=" + downloadTime +
                ", dir=" + dir +
                ", type=" + type +
                '}';
    }

    public boolean getDir() {
        return this.dir;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
