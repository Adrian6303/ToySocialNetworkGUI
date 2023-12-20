package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PrietenieDBRepository implements Repository<Tuple<Long, Long>, Prietenie> {

    private final String url;
    private final String username;
    private final String password;
    private PrietenieValidator validator;
    public PrietenieDBRepository(String url, String username, String password, PrietenieValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Prietenie> findOne(Tuple<Long, Long> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from prietenii where id1 = ? and id2 = ?")

        ) {
            Long id1 = id.getLeft();
            Long id2 = id.getRight();
            statement.setInt(1, Math.toIntExact(id.getLeft()));
            statement.setInt(2, Math.toIntExact(id.getRight()));
            try {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    LocalDateTime d = resultSet.getDate("date").toLocalDate().atStartOfDay();
                    Prietenie p = new Prietenie(id1, id2, d);
                    return Optional.ofNullable(p);
                }
            }
            catch(RuntimeException e){
                return Optional.empty();
            }

        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }


    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from prietenii")

        ) {
            try {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long id1 = resultSet.getLong("id1");
                    long id2 = resultSet.getLong("id2");
                    LocalDateTime d = resultSet.getDate("date").toLocalDate().atStartOfDay();
                    Prietenie p = new Prietenie(id1, id2, d);
                    prietenii.add(p);
                }
                return prietenii;
            }
            catch(RuntimeException e ){
                return null;
            }

        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into prietenii(id1, id2, date) VALUES (?,?,?)")
        ) {
            statement.setLong(1, entity.getFirst());
            statement.setLong(2, entity.getSecond());
            statement.setDate(3, Date.valueOf(entity.getDate().toLocalDate()));
            try{
                if (statement.executeUpdate() <= 0) {
                    throw new RuntimeException("Nu se salveaza");
                }
            }
            catch(RuntimeException e){
                return Optional.empty();
            }

        } catch (SQLException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }


    @Override
    public Optional<Prietenie> delete(Prietenie entity) {
        Long id1 = entity.getFirst();
        Long id2 = entity.getSecond();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from prietenii where id1=? and id2 =?")
        ){

            statement.setLong(1,id1);
            statement.setLong(2, id2);
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a sters!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
       return Optional.empty();
    }
}
