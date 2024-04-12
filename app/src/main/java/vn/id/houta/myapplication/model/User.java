package vn.id.houta.myapplication.model;

public class User {
    private String userid, email, name;
    private int age, gender;

    // Used for Database Casting, do not delete
    public User() {}

    public User(String name, int gender, int age) {this.name = name;this.gender = gender;this.age = age;}
    public String getEmail() {
        return email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}