package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.repository.InMemoryRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;
import com.example.toysocialnetworkgui.repository.paging.Page;
import com.example.toysocialnetworkgui.repository.paging.Pageable;
import com.example.toysocialnetworkgui.utils.Observable;
import com.example.toysocialnetworkgui.utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtilizator implements Observable {
    private UserDBRepository repo_utilizatori;
    private PrietenieDBRepository repo_prietenii;
    private List<Observer> observers = new ArrayList<>();
    public ServiceUtilizator(UserDBRepository repo_utilizatori, PrietenieDBRepository repo_prietenii){
        this.repo_utilizatori = repo_utilizatori;
        this.repo_prietenii = repo_prietenii;
    }
    public Utilizator findOne(Long id){
        return repo_utilizatori.findOne(id).orElseThrow();
    }

    public Iterable<Utilizator> findAll(){
        return repo_utilizatori.findAll();
    }

    public void save(Utilizator entity) throws Throwable{
        if(repo_utilizatori.save(entity).isPresent())
            throw new Throwable("Nu s-a adaugat");
        notifyObservers();
    }

    public boolean delete(Long id){
        if(repo_utilizatori.delete(repo_utilizatori.findOne(id).get()).isPresent()){
            notifyObservers();
            return true;
        }
        else
            return false;
    }

    public Utilizator update(Utilizator entity){
        Utilizator u = repo_utilizatori.update(entity).orElse(null);
        notifyObservers();
        return u;
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

    public Page<Utilizator> getUsersOnPage(Pageable pageable){
        return repo_utilizatori.findAll_page(pageable);
    }


}
