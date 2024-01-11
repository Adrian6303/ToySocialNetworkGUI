package com.example.toysocialnetworkgui.domain;

import java.util.Objects;

public class Login extends Entity<Long> {
    private String username;
    private String password;
    private Long id;

    public Login(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(username, login.username) && Objects.equals(password, login.password) && Objects.equals(id, login.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, id);
    }
}
