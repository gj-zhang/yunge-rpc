package com.rancho.yunge.demo.protocol;

/**
 * @author zgj-18063794
 * @createTime 2019-01-22 15:25
 */
public class UserProtocol {

    private String name;
    private int age;
    private String gender;
    private boolean handsome;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isHandsome() {
        return handsome;
    }

    public void setHandsome(boolean handsome) {
        this.handsome = handsome;
    }
}
