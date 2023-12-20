package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.validators.CerereValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDBRepository implements Repository<Long, Message>{

    private final String url;
    private final String username;
    private final String password;
    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from mesaj where id_mesaj = ?")

        ) {
            statement.setInt(1, Math.toIntExact(id));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {

                long id_to = resultSet.getLong("id_to");
                long id_from = resultSet.getLong("id_from");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime d = resultSet.getDate("data").toLocalDate().atStartOfDay();
                Message m = new Message(id_from,id_to);
                m.setMessage(mesaj);
                m.setData(d);
                return Optional.ofNullable(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }


    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from mesaj");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                long id = resultSet.getLong("id_mesaj");

                long id_to = resultSet.getLong("id_to");
                long id_from = resultSet.getLong("id_from");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime d = resultSet.getDate("data").toLocalDate().atStartOfDay();
                Message m = new Message(id_from,id_to);
                m.setId(id);
                m.setId_from(id_from);
                m.setId_to(id_to);
                m.setMessage(mesaj);
                m.setData(d);
                messages.add(m);
            }
            return messages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into mesaj(id_to,id_from,mesaj,data) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, entity.getId_to());
            statement.setLong(2, entity.getId_from());
            statement.setString(3, entity.getMessage());
            statement.setDate(4, Date.valueOf(entity.getData().toLocalDate()));

            if (statement.executeUpdate() <= 0) {
                throw new RuntimeException("Nu se salveaza");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Message entity) {
        Long id = entity.getId();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from mesaj where id_mesaj=?",Statement.RETURN_GENERATED_KEYS)
        ){

            statement.setLong(1,id);
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a sters!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.of(entity);
    }
}
