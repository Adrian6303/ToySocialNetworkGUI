package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.domain.Cerere;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.ServiceCerere;
import com.example.toysocialnetworkgui.service.ServicePrietenie;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import com.example.toysocialnetworkgui.utils.Observer;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class UserRequestsController implements Observer {
    public ListView<Cerere> receivedListView;
    public ListView<Utilizator> sentListView;
    ObservableList<Utilizator> sentModel = FXCollections.observableArrayList();
    ObservableList<Cerere> receivedModel = FXCollections.observableArrayList();
    private ServiceCerere serviceCerere;
    private ServicePrietenie servicePrietenie;
    private ServiceUtilizator serviceUser;
    private Utilizator user;

    public void setServiceCerere(ServiceCerere serviceCerere, ServicePrietenie servicePrietenie, ServiceUtilizator serviceUser) {
        this.serviceCerere = serviceCerere;
        this.servicePrietenie = servicePrietenie;
        this.serviceUser = serviceUser;
        serviceCerere.addObserver(this);
        initData();
    }

    public void setUser(Utilizator utilizator)
    {
        user = utilizator;
    }

    public void initialize()
    {
        receivedListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Cerere> call(ListView param) {
                return new ListCell<>(){
                    @Override
                    public void updateItem(Cerere cerere, boolean empty) {
                        super.updateItem(cerere, empty);
                        if (empty || cerere == null) {
                            setText(null);
                        } else {
                            Long userID = cerere.getFirst();
                            Utilizator utilizator = serviceUser.findOne(userID);
                            setText(utilizator.getFirstName()+" "+utilizator.getLastName());
                        }
                    }
                };
            }
        });
        sentListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Utilizator> call(ListView param) {
                return new ListCell<>(){
                    @Override
                    public void updateItem(Utilizator utilizator, boolean empty) {
                        super.updateItem(utilizator, empty);
                        if (empty || utilizator == null) {
                            setText(null);
                        } else {
                            setText(utilizator.getFirstName()+" "+utilizator.getLastName());
                        }
                    }
                };
            }
        });
        receivedListView.setItems(receivedModel);
        sentListView.setItems(sentModel);
    }

    public void initData()
    {
        receivedModel.setAll(StreamSupport.stream(serviceCerere.findAll().spliterator(), false).filter(
                c -> c.getSecond().equals(user.getId()) && Objects.equals(c.getStatus(), "in asteptare")
        ).toList());
        sentModel.setAll(StreamSupport.stream(serviceUser.findAll().spliterator(), false).filter(
                u -> !Objects.equals(u.getId(), user.getId())
        ).filter(
                u -> !user.getFriends().contains(u)
        ).filter(
                u -> StreamSupport.stream(serviceCerere.findAll().spliterator(), false).noneMatch(c -> (c.getFirst() == u.getId() && c.getSecond() == user.getId()) || (c.getFirst() == user.getId() && c.getSecond() == u.getId()))
        ).toList());
    }


    @Override
    public void update() {
        initData();
        receivedListView.setItems(receivedModel);
        sentListView.setItems(sentModel);
    }

    public void actionButtonHandle() {
        Utilizator selectedUser = sentListView.selectionModelProperty().get().getSelectedItem();
        if (selectedUser != null)
        {
            serviceCerere.save(user.getId(), selectedUser.getId());
        }
    }

    public void acceptButtonHandle() {
        Cerere cerere = receivedListView.selectionModelProperty().get().getSelectedItem();
        if(cerere != null)
        {
            serviceCerere.update(cerere.getId(), "acceptata");

        }
    }

    public void refuseButtonHandle() {
        Cerere cerere = receivedListView.selectionModelProperty().get().getSelectedItem();
        if(cerere != null)
        {
            serviceCerere.update(cerere.getId(), "respinsa");
        }
    }
}
