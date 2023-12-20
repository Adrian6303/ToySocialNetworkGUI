package com.example.toysocialnetworkgui.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<Utilizator> friends = new ArrayList<>();

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setID(Long id){this.id = id;}

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void addFriend(Utilizator u){
        this.friends.add(u);
    }

    public void deleteFriend(Utilizator u){
        this.friends.remove(u);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(firstName  +
                " " + lastName);
        for(Utilizator friend : friends)
        {
            s.append(friend.firstName).append(" ").append(friend.lastName).append(", ");
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getFirstName(), getLastName());
    }
}
