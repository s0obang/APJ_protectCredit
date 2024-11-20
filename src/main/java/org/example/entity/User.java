package org.example.entity;

public class User {
    private Integer userId;
    private String nickname;
    private String password;


    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public User(int id, String nickname, String password) {
        this.userId = id;
        this.nickname = nickname;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
