package com.xn.rxjava.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xn068074 on 2018/7/24.
 */

public class Student extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String address;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name:");
        sb.append(name);
        sb.append("age:");
        sb.append(this.age);
        return sb.toString();
    }
}
