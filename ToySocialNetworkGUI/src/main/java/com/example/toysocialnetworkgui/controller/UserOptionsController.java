package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.HelloApplication;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.ServiceCerere;
import com.example.toysocialnetworkgui.service.ServiceMessage;
import com.example.toysocialnetworkgui.service.ServicePrietenie;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserOptionsController {

    private Utilizator user;

    private ServiceUtilizator serviceUtilizator;
    private ServicePrietenie servicePrietenie;
    private ServiceCerere serviceCerere;

    private ServiceMessage serviceMessage;


    public void setUser(Utilizator utilizator)
    {
        user = utilizator;
    }

    public void setService(ServiceCerere serviceCerere,ServiceMessage serviceMessage,ServicePrietenie servicePrietenie, ServiceUtilizator serviceUser) {
        this.serviceMessage = serviceMessage;
        this.serviceUtilizator = serviceUser;
        this.serviceCerere = serviceCerere;
        this.servicePrietenie=servicePrietenie;
    }

    public void requestsButtonHandle() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-requests-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cereri User");
        UserRequestsController userRequestsController = fxmlLoader.getController();
        userRequestsController.setUser(user);
        userRequestsController.setServiceCerere(serviceCerere, servicePrietenie, serviceUtilizator);
        stage.setScene(scene);
        stage.show();

    }

    public void messagesButtonHandle() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-messages-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mesaje");
        UserMessagesController userMessagesController = fxmlLoader.getController();
        userMessagesController.setServiceMessages(serviceMessage,serviceUtilizator);
        userMessagesController.setUser(user);
        stage.setScene(scene);
        stage.show();
    }
}
