package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.controller.UserController;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.validators.CerereValidator;
import com.example.toysocialnetworkgui.domain.validators.PrietenieValidator;
import com.example.toysocialnetworkgui.domain.validators.UtilizatorValidator;
import com.example.toysocialnetworkgui.repository.CerereDBRepository;
import com.example.toysocialnetworkgui.repository.MessageDBRepository;
import com.example.toysocialnetworkgui.repository.PrietenieDBRepository;
import com.example.toysocialnetworkgui.repository.UserDBRepository;
import com.example.toysocialnetworkgui.service.ServiceCerere;
import com.example.toysocialnetworkgui.service.ServiceMessage;
import com.example.toysocialnetworkgui.service.ServicePrietenie;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private UserDBRepository userRepo;
    private PrietenieDBRepository prietenieRepo;
    private CerereDBRepository cerereRepo;
    private MessageDBRepository messageRepo;
    private ServiceUtilizator serviceUtilizator;
    private ServicePrietenie servicePrietenie;
    private ServiceCerere serviceCerere;
    private ServiceMessage serviceMessage;

        @Override
    public void start(Stage stage) throws IOException {
        userRepo = new UserDBRepository(
                "jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",
                "Adrian03",
                new UtilizatorValidator());
        prietenieRepo = new PrietenieDBRepository(
                "jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",
                "Adrian03",
                new PrietenieValidator());
        cerereRepo = new CerereDBRepository(
                "jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",
                "Adrian03",
                new CerereValidator()
        );
        messageRepo = new MessageDBRepository(
                "jdbc:postgresql://localhost:5432/SocialNetwork",
                "postgres",
                "Adrian03"
            );
        serviceUtilizator = new ServiceUtilizator(userRepo, prietenieRepo);
        servicePrietenie = new ServicePrietenie(userRepo, prietenieRepo);
        serviceCerere = new ServiceCerere(userRepo, prietenieRepo, cerereRepo);
        serviceMessage = new ServiceMessage(messageRepo, userRepo);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        UserController userController = fxmlLoader.getController();
        userController.setServiceUtilizator(serviceUtilizator);
        userController.setServicePrietenie(servicePrietenie);
        userController.setServiceCerere(serviceCerere);
        userController.setServiceMessage(serviceMessage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}