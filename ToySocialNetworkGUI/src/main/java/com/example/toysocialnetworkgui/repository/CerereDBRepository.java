package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.validators.CerereValidator;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CerereDBRepository implements Repository<Tuple<Long, Long>, Cerere>{
    private final String url;
    private final String username;
    private final String password;
    private CerereValidator validator;
    public CerereDBRepository(String url, String username, String password, CerereValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Cerere> findOne(Tuple<Long, Long> id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from cerere where id1 = ? and id2 = ?")

        ) {
            Long id1 = id.getLeft();
            Long id2 = id.getRight();
            statement.setInt(1, Math.toIntExact(id.getLeft()));
            statement.setInt(2,Math.toIntExact(id.getRight()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Cerere c = new Cerere(id1,id2);
                String s = resultSet.getString("status");
                c.setStatus(s);
                return Optional.ofNullable(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Cerere> findAll() {
        Set<Cerere> cereri = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from cerere");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                long id1= resultSet.getLong("id1");
                long id2= resultSet.getLong("id2");
                String s = resultSet.getString("status");
                Cerere c = new Cerere(id1,id2);
                c.setStatus(s);
                cereri.add(c);
            }
            return cereri;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Cerere> save(Cerere entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into cerere(id1, id2,status) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, entity.getFirst());
            statement.setLong(2, entity.getSecond());
            statement.setString(3, entity.getStatus());

            if (statement.executeUpdate() <= 0) {
                throw new RuntimeException("Nu se salveaza");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Cerere> delete(Cerere entity) {
        Long id1 = entity.getFirst();
        Long id2 = entity.getSecond();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from cerere where id1=? and id2 =?",Statement.RETURN_GENERATED_KEYS)
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
    public Optional<Cerere> update(Cerere entity) {
        Long id1 = entity.getFirst();
        Long id2 = entity.getSecond();
        String status_nou= entity.getStatus();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("update cerere set status=? where id1=? and id2 =?",Statement.RETURN_GENERATED_KEYS)
        ){

            statement.setString(1,status_nou);
            statement.setLong(2,id1);
            statement.setLong(3,id2);
            if(statement.executeUpdate()<=0) {

                throw new RuntimeException("Nu s-a modificat!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }
}
