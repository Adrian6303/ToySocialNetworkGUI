package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.HelloApplication;
import com.example.toysocialnetworkgui.domain.Login;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLoginController {

    public TextField usernameTextField;
    public PasswordField passwordTextField;
    private ServiceLogin serviceLogin;
    private Utilizator user;

    private ServiceUtilizator serviceUtilizator;
    private ServicePrietenie servicePrietenie;
    private ServiceCerere serviceCerere;

    private ServiceMessage serviceMessage;



    public void setService(ServiceCerere serviceCerere,ServiceMessage serviceMessage,ServicePrietenie servicePrietenie, ServiceUtilizator serviceUser) {
        this.serviceMessage = serviceMessage;
        this.serviceUtilizator = serviceUser;
        this.serviceCerere = serviceCerere;
        this.servicePrietenie=servicePrietenie;
    }

    public void setServiceLogin(ServiceLogin serviceLogin) {
        this.serviceLogin = serviceLogin;
    }

    public void setUser(Utilizator utilizator)
    {
        user = utilizator;
    }


    public void loginButtonHandler( ) throws Throwable {
        try {
            Login login_nou = new Login(0l," "," ");
            login_nou = serviceLogin.findOne(user.getId());
            if(login_nou.getUsername().equals(usernameTextField.getText()) && login_nou.getPassword().equals(passwordTextField.getText()))
            {
                this.openWindow();
            }

        }
        catch(Exception e){
            serviceLogin.save(new Login(user.getId(), usernameTextField.getText(), passwordTextField.getText()));
            this.openWindow();
        }
    }

    private void openWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-options-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Optiuni");
        UserOptionsController userOptionsController = fxmlLoader.getController();
        userOptionsController.setUser(user);
        userOptionsController.setService(serviceCerere, serviceMessage, servicePrietenie, serviceUtilizator);
        stage.setScene(scene);
        stage.show();
    }
}
