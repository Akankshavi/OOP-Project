package com.example.loginapp;

public class Credentials {
    String name = "Akanksha Hublikar";
    String password = "123456";
    Credentials(String Username, String Password)
    {
        this.name=Username;
        this.password=Password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
