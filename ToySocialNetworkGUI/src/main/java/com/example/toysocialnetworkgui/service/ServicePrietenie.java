package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.domain.validators.ValidationException;
import com.example.toysocialnetworkgui.repository.InMemoryRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ServicePrietenie{
    private String url;
    private String username;
    private String password;
    UserDBRepository repo_utilizatori = new UserDBRepository(url, username, password, new UtilizatorValidator());
    PrietenieDBRepository repo_prietenii = new PrietenieDBRepository(url, username,password,new PrietenieValidator());

    public ServicePrietenie(UserDBRepository repo_utilizatori, PrietenieDBRepository repo_prietenii) {
        this.repo_utilizatori = repo_utilizatori;
        this.repo_prietenii = repo_prietenii;
    }

    public Prietenie findOne(Tuple<Long, Long> id) {
        try {
            return repo_prietenii.findOne(id).orElseThrow(() -> new Throwable("plm"));
        } catch (Throwable v) {
            return null;
        }
    }

    public Iterable<Prietenie> findAll() {return repo_prietenii.findAll();}

    public Prietenie save(Long id1, Long id2) {
        try {
            Utilizator u1 = repo_utilizatori.findOne(id1).orElseThrow(() -> new Throwable("Nu exista"));
            Utilizator u2 = repo_utilizatori.findOne(id2).orElseThrow(() -> new Throwable("Nu exista"));
            Prietenie p = new Prietenie(id1, id2, LocalDateTime.now());
            repo_prietenii.save(p);
            u1.addFriend(u2);
            u2.addFriend(u1);
            return p;
        } catch (Throwable v) {
            return null;
        }

    }

    public Prietenie delete(Tuple<Long, Long> id) {
        try {
            Utilizator u1 = repo_utilizatori.findOne(id.getLeft()).orElseThrow(() -> new Throwable("Nu exista"));
            Utilizator u2 = repo_utilizatori.findOne(id.getRight()).orElseThrow(() -> new Throwable("Nu exista"));
            u1.deleteFriend(u2);
            u2.deleteFriend(u1);
            return repo_prietenii.delete(repo_prietenii.findOne(id).get()).orElseThrow(() -> new Throwable("Nu exista"));
        } catch (Throwable v) {
            return null;
        }
    }

    public int comunitati() {
        Iterable<Utilizator> users = repo_utilizatori.findAll();
        HashMap<Utilizator, Integer> pairs = new HashMap<>();
        for (Utilizator user : users) {
            pairs.put(user, 0);
        }
        int k = 0;
        for (Utilizator user : users) {
            if (pairs.get(user) != 0 || user.getFriends().isEmpty()) {
                continue;
            }
            k++;
            Stack<Utilizator> stack = new Stack<>();
            stack.push(user);
            while (!stack.empty()) {
                Utilizator u = stack.pop();
                pairs.put(u, k);
                for (Utilizator friend : u.getFriends()) {
                    if (pairs.get(friend) == 0)
                        stack.push(friend);
                }
            }
        }
        return k;
    }

    public List<Utilizator> mostSociable()
    {
        Iterable<Utilizator> users = repo_utilizatori.findAll();
        HashMap<Utilizator, Integer> pairs = new HashMap<>();
        for (Utilizator user : users) {
            pairs.put(user, 0);
        }
        int k = 0;
        int max = 0;
        int maxK = 0;
        for (Utilizator user : users) {
            if (pairs.get(user) != 0 || user.getFriends().isEmpty()) {
                continue;
            }
            k++;
            Stack<Utilizator> stack = new Stack<>();
            int dist = 0;
            stack.push(user);
            while (!stack.empty()) {
                Utilizator u = stack.pop();
                dist++;
                if(dist > max)
                {
                    max = dist;
                    maxK = k;
                }
                pairs.put(u, k);
                for (Utilizator friend : u.getFriends()) {
                    if (pairs.get(friend) == 0)
                        stack.push(friend);
                }
            }
        }
        List<Utilizator> rez = new ArrayList<>();
        for(Utilizator user : users)
        {
            if(pairs.get(user) == maxK)
                rez.add(user);
        }
        return rez;
    }
}
