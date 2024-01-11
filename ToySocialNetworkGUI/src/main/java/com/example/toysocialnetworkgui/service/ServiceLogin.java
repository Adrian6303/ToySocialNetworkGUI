package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Login;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.repository.LoginDBRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;
import com.example.toysocialnetworkgui.utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class ServiceLogin {

    private UserDBRepository repo_utilizatori;

    private LoginDBRepository repo_login;
    public ServiceLogin(UserDBRepository repo_utilizatori, LoginDBRepository repo_login){
        this.repo_utilizatori = repo_utilizatori;
        this.repo_login = repo_login;
    }

    public Login findOne(Long id){
        return repo_login.findOne(id).orElseThrow();
    }

    public Iterable<Login> findAll(){
        return repo_login.findAll();
    }

    public void save(Login entity) throws Throwable{
        if(repo_login.save(entity).isPresent())
            throw new Throwable("Nu s-a adaugat");
    }

    public boolean delete(Long id){
        if(repo_login.delete(repo_login.findOne(id).get()).isPresent()){
            return true;
        }
        else
            return false;
    }
}
