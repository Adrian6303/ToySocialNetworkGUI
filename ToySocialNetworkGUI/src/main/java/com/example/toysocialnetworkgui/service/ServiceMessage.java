package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.repository.CerereDBRepository;
import com.example.toysocialnetworkgui.repository.MessageDBRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;
import com.example.toysocialnetworkgui.utils.Observable;
import com.example.toysocialnetworkgui.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class ServiceMessage implements Observable {

    private String url;
    private String username;
    private String password;

    private UserDBRepository repo_utilizatori = new UserDBRepository(url, username, password, new UtilizatorValidator());
    private MessageDBRepository repo_mesaj = new MessageDBRepository(url,username, password);
    private List<Observer> observers = new ArrayList<>();

    public ServiceMessage(MessageDBRepository repo_mesaj , UserDBRepository repo_utilizatori) {
        this.repo_utilizatori = repo_utilizatori;
        this.repo_mesaj = repo_mesaj;
    }

    public Iterable<Message> findAll() {
        return repo_mesaj.findAll();
    }

    public Message save(Long id_to, Long id_from, String message) {
        try {
            Message m = new Message(0l,0l);
            m.setId_to(id_to);
            m.setId_from(id_from);
            m.setMessage(message);
            m.setData(LocalDateTime.now());
            repo_mesaj.save(m);
            notifyObservers();
            return m;
        } catch (Throwable v) {
            return null;
        }

    }
    
    public Iterable<Message> filtrare(Long id1, Long id2){
        return StreamSupport.stream(repo_mesaj.findAll().spliterator(), false).
                filter(m -> (Objects.equals(m.getId_to(), id1) && Objects.equals(m.getId_from(), id2)) || (Objects.equals(m.getId_from(), id1) && Objects.equals(m.getId_to(), id2))).
                sorted(Comparator.comparing(Message::getId)).toList();
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
