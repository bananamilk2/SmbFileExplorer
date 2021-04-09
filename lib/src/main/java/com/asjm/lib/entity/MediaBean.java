package com.asjm.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class MediaBean implements Parcelable {

    private String name;
    private int index;

    public MediaBean(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }


    protected MediaBean(Parcel in) {
        name = in.readString();
        index = in.readInt();
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
        @Override
        public MediaBean createFromParcel(Parcel in) {
            return new MediaBean(in);
        }

        @Override
        public MediaBean[] newArray(int size) {
            return new MediaBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaBean mediaBean = (MediaBean) o;
        return index == mediaBean.index &&
                Objects.equals(name, mediaBean.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index);
    }
}
