package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Login;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LoginDBRepository implements Repository<Long, Login> {

    private String url;
    private String username_db;
    private String password_db;
    public LoginDBRepository(String url, String username, String password ) {
        this.url = url;
        this.username_db = username;
        this.password_db = password;
    }

    @Override
    public Optional<Login> findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username_db, password_db);
            PreparedStatement statement = connection.prepareStatement("select * from login " +
                    "where id_user = ?", Statement.RETURN_GENERATED_KEYS);

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Login l = new Login(longID,username,password);
                l.setId(longID);
                return Optional.ofNullable(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Login> findAll() {
        Set<Login> logins = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username_db, password_db);
             PreparedStatement statement = connection.prepareStatement("select * from login");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id_user= resultSet.getLong("id_user");
                String username =resultSet.getString("username");
                String password =resultSet.getString("password");
                Login login=new Login(id_user,username,password);
                logins.add(login);

            }
            return logins;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Login> save(Login entity) {
        try (Connection connection = DriverManager.getConnection(url, username_db, password_db);
             PreparedStatement statement = connection.prepareStatement("insert into login(id_user,username,password) values (?,?,?)",Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getUsername());
            statement.setString(3, entity.getPassword());
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a salvat!");
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Login> delete(Login entity) {
        Long id_user = entity.getId();
        try (Connection connection = DriverManager.getConnection(url, username_db, password_db);
             PreparedStatement statement = connection.prepareStatement("delete from login where id_user=?");
        ){

            statement.setInt(1, Math.toIntExact(id_user));
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a sters!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Login> update(Login entity) {
        return Optional.empty();
    }
}
