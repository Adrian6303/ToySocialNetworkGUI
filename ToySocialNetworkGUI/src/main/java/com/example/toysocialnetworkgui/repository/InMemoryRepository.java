//package repository;
//
//import domain.Entity;
//import domain.validators.Validator;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
//    private Validator<E> validator;
//    Map<ID,E> entities;
//
//    public InMemoryRepository(Validator<E> validator) {
//        this.validator = validator;
//        entities=new HashMap<ID,E>();
//    }
//
//    @Override
//    public E findOne(ID id){
//        if (id==null)
//            throw new IllegalArgumentException("id must be not null");
//        return entities.get(id);
//    }
//
//    @Override
//    public Iterable<E> findAll() {
//        return entities.values();
//    }
//
//    @Override
//    public E save(E entity) {
//        if (entity==null)
//            throw new IllegalArgumentException("entity must be not null");
//        validator.validate(entity);
//        long id_caut = 0;
//
//
//        else entities.put(entity.getId(),entity);
//        return null;
//    }
//
//    @Override
//    public E delete(ID id) {
//        if (id==null)
//            throw new IllegalArgumentException("entity must be not null");
//        if(entities.get(id) == null) {
//            return findOne(id);
//        }
//        else entities.remove(id);
//
//        return null;
//    }
//
//    @Override
//    public E update(E entity) {
//
//        if(entity == null)
//            throw new IllegalArgumentException("entity must be not null!");
//        validator.validate(entity);
//
//        entities.put(entity.getId(),entity);
//
//        if(entities.get(entity.getId()) != null) {
//            entities.put(entity.getId(),entity);
//            return null;
//        }
//        return entity;
//
//    }
//}
package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.domain.validators.ValidationException;
import com.example.toysocialnetworkgui.domain.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @param <ID>
 * @param <E>
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    public Long getAvailableID(){
        long id = 1;
        while(entities.get(id)!=null)
            id++;
        return id;
    }
    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");

        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        return Optional.ofNullable(entities.remove(entity.getId()));
    }

    @Override
    public Optional<E> update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        return Optional.ofNullable(entities.put(entity.getId(),entity));
    }
}