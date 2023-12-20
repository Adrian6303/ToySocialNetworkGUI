package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.domain.validators.CerereValidator;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.repository.CerereDBRepository;
import com.example.toysocialnetworkgui.repository.InMemoryRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;
import com.example.toysocialnetworkgui.utils.Observable;
import com.example.toysocialnetworkgui.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceCerere implements Observable {

    private String url;
    private String username;
    private String password;
    private
    UserDBRepository repo_utilizatori = new UserDBRepository(url, username, password, new UtilizatorValidator());
    PrietenieDBRepository repo_prietenii = new PrietenieDBRepository(url, username, password, new PrietenieValidator());

    CerereDBRepository repo_cereri = new CerereDBRepository(url, username,password,new CerereValidator());

    private List<Observer> observers = new ArrayList<>();

    public ServiceCerere(UserDBRepository repo_utilizatori, PrietenieDBRepository repo_prietenii, CerereDBRepository repo_cereri) {
        this.repo_utilizatori = repo_utilizatori;
        this.repo_prietenii = repo_prietenii;
        this.repo_cereri = repo_cereri;
    }

    public Cerere findOne(Tuple<Long, Long> id) {
        try {
            return repo_cereri.findOne(id).orElseThrow(() -> new Throwable("Nu exista!"));
        } catch (Throwable v) {
            return null;
        }
    }

    public Iterable<Cerere> findAll() {
        return repo_cereri.findAll();
    }

    public Cerere save(Long id1, Long id2) {
        try {
            Cerere c = new Cerere(id1, id2);
            repo_cereri.save(c);
            notifyObservers();
            return c;
        } catch (Throwable v) {
            return null;
        }

    }

    public Cerere delete(Tuple<Long, Long> id) {
        try {

            return repo_cereri.delete(repo_cereri.findOne(id).get()).orElseThrow(() -> new Throwable("Nu exista"));
        } catch (Throwable v) {
            return null;
        }
    }

    public Cerere update(Tuple<Long,Long> id, String status){
        try {
            Cerere c = repo_cereri.findOne(id).get();
            c.setStatus(status);
            repo_cereri.update(c);
            //c.setStatus(status);
            if(status=="acceptata"){

                Utilizator u1 = repo_utilizatori.findOne(c.getFirst()).orElseThrow(() -> new Throwable("Nu exista"));
                Utilizator u2 = repo_utilizatori.findOne(c.getSecond()).orElseThrow(() -> new Throwable("Nu exista"));
                Prietenie p = new Prietenie(c.getFirst(), c.getSecond(), LocalDateTime.now());
                repo_prietenii.save(p);
                u1.addFriend(u2);
                u2.addFriend(u1);
            }
            notifyObservers();
            return c;
        } catch (Throwable v) {
            return null;
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
