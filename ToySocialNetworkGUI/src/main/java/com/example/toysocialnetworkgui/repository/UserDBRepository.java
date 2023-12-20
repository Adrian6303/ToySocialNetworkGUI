package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.repository.paging.Page;
import com.example.toysocialnetworkgui.repository.paging.Pageable;

import java.sql.*;
import java.util.*;
import java.util.Optional;

public class UserDBRepository implements Repository<Long, Utilizator>{

    private String url;
    private String username;
    private String password;
    private UtilizatorValidator validator;
    public UserDBRepository(String url, String username, String password, UtilizatorValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Utilizator> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator u = new Utilizator(firstName,lastName);
                u.setId(longID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                Utilizator user=new Utilizator(firstName,lastName);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Optional<Utilizator> save(Utilizator entity) {

            validator.validate(entity);

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("insert into users(first_name,last_name) values (?,?)",Statement.RETURN_GENERATED_KEYS);
                ){

                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                if(statement.executeUpdate()<=0) {

                    throw new RuntimeException("Nu s-a salvat!");
                }
                return Optional.empty();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public Optional<Utilizator> delete(Utilizator entity) {
        validator.validate(entity);
        Long id = entity.getId();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from users where id=?");
        ){

            statement.setInt(1, Math.toIntExact(id));
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a sters!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> update(Utilizator user) {
        Long id = user.getId();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET first_name=?, last_name=? WHERE id=?",Statement.RETURN_GENERATED_KEYS)

        ) {
            statement.setString(1,user.getFirstName());
            statement.setString(2,user.getLastName());
            statement.setLong(3,user.getId());
            try {
                if (statement.executeUpdate() > 0)
                    return Optional.empty();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(user);
    }

    private int returnNumberOfElements(){
        int numberOfElements=0;
        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             // pas 2: design & execute SLQ
             PreparedStatement statement = connection.prepareStatement("select count(*) as count from users");
             ResultSet resultSet = statement.executeQuery();
        ) {
            // pas 3: process result set
            while (resultSet.next()){
                numberOfElements = resultSet.getInt("count");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        // pas 3: return no elements
        return numberOfElements;
    }


    public Page<Utilizator> findAll_page(Pageable pageable) {
        int numberOfElements = returnNumberOfElements();
        int limit = pageable.getPageSize();
        int offset = pageable.getPageSize()*pageable.getPageNumber();
        //System.out.println(offset + " ?>= "+numberOfElements);
        if(offset >= numberOfElements)
            return new Page<>(new ArrayList<>(), numberOfElements);
        // prereq: create empty set
        List<Utilizator> users = new ArrayList<>();
        // pas 1: conectarea la baza de date
        try (Connection connection =
                     DriverManager.getConnection(url,username, password);
             // pas 2: design & execute SLQ
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?");
        ) {
            statement.setInt(2, offset);
            statement.setInt(1,limit);
            ResultSet resultSet = statement.executeQuery();
            // pas 3: process result set
            while (resultSet.next()){

                Long id= resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator currentUser = new Utilizator(firstName, lastName);
                currentUser.setId(id);
                // add to set of movies
                users.add(currentUser);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        // pas 3: return result list
        return new Page<>(users, numberOfElements);
    }
}
