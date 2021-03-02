package com.asjm.fileexplorer.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "tableFiles")
public class FileSmb {
    @Id(autoincrement = true)
    private Long id;

    @Property
    private int index;

    @Index(unique = true)
    private String fileHash;

    @Property
    private String fileSource;
    @Property
    private String fileName;
    @Property
    private Long fileSize;
    @Property
    private Date fileTime;
    @Property
    private Date downloadTime;
    @Property
    private boolean dir;
    @Property
    private int fileType;

    @Generated(hash = 1924370362)
    public FileSmb(Long id, int index, String fileHash, String fileSource,
            String fileName, Long fileSize, Date fileTime, Date downloadTime,
            boolean dir, int fileType) {
        this.id = id;
        this.index = index;
        this.fileHash = fileHash;
        this.fileSource = fileSource;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileTime = fileTime;
        this.downloadTime = downloadTime;
        this.dir = dir;
        this.fileType = fileType;
    }

    @Generated(hash = 2141355914)
    public FileSmb() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileHash() {
        return this.fileHash;
    }
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
    public String getFileSource() {
        return this.fileSource;
    }
    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Long getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public Date getFileTime() {
        return this.fileTime;
    }
    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
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
    public int getFileType() {
        return this.fileType;
    }
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean getDir() {
        return this.dir;
    }

    @Override
    public String toString() {
        return "FileSmb{" +
                "id=" + id +
                ", index=" + index +
                ", fileHash='" + fileHash + '\'' +
                ", fileSource='" + fileSource + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileTime=" + fileTime +
                ", downloadTime=" + downloadTime +
                ", dir=" + dir +
                ", fileType=" + fileType +
                '}';
    }
}
