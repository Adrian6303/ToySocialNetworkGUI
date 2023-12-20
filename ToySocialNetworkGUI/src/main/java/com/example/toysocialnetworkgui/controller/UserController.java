package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.HelloApplication;
import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.ServiceCerere;
import com.example.toysocialnetworkgui.service.ServiceMessage;
import com.example.toysocialnetworkgui.service.ServicePrietenie;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import com.example.toysocialnetworkgui.utils.Observer;
import com.example.toysocialnetworkgui.repository.paging.Page;
import com.example.toysocialnetworkgui.repository.paging.Pageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class UserController implements Observer {
    public TableView<Utilizator> usersTableView;
    public TableColumn<Utilizator, String> firstNameColumn;
    public TableColumn<Utilizator, String> lastNameColumn;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    private ServiceUtilizator serviceUtilizator;
    private ServicePrietenie servicePrietenie;
    private ServiceCerere serviceCerere;
    private ServiceMessage serviceMessage;

    @FXML
    Button previousButton;

    @FXML
    Button nextButton;

    @FXML
    Button moreButton;

    @FXML
    Button lessButton;

    int currentPage=0;

    int numberOfRecordsPerPage=5;

    int totalNumberOfElements;

    public void setServiceUtilizator(ServiceUtilizator serviceUtilizator) {
        this.serviceUtilizator = serviceUtilizator;
        serviceUtilizator.addObserver(this);
        initData();
    }

    public void setServicePrietenie(ServicePrietenie servicePrietenie) {
        this.servicePrietenie = servicePrietenie;
    }

    public void setServiceCerere(ServiceCerere serviceCerere) {
        this.serviceCerere = serviceCerere;
    }

    public void setServiceMessage(ServiceMessage serviceMessage) {this.serviceMessage = serviceMessage;}

    public void initialize()
    {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        usersTableView.setItems(model);
    }

    public void initData()
    {
        Page<Utilizator> usersOnCurrentPage = serviceUtilizator.getUsersOnPage(new Pageable(currentPage, numberOfRecordsPerPage));
        totalNumberOfElements = usersOnCurrentPage.getTotalNumberOfElements();
        model.setAll(StreamSupport.stream(usersOnCurrentPage.getElementsOnPage().spliterator(), false).toList());
        handlePageNavigationChecks();
        handleRecordsPerPageChecks();
    }

    public void deleteButtonHandle(ActionEvent actionEvent) {
        Utilizator deletedUser = usersTableView.selectionModelProperty().get().getSelectedItem();
        if(deletedUser != null)
        {
            serviceUtilizator.delete(deletedUser.getId());
        }
    }

    @Override
    public void update() {
        initData();
    }

    public void addButtonHandle() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-edit-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Add User");
        UserEditController userEditController = fxmlLoader.getController();
        userEditController.setUtilizator(null);
        userEditController.setServiceUtilizator(serviceUtilizator);
        stage.setScene(scene);
        stage.show();
    }

    public void editButtonHandle() throws IOException {
        Utilizator updatedUser = usersTableView.selectionModelProperty().get().getSelectedItem();
        if(updatedUser != null)
        {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-edit-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Edit User");
            UserEditController userEditController = fxmlLoader.getController();
            userEditController.setUtilizator(updatedUser);
            userEditController.setServiceUtilizator(serviceUtilizator);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void requestsButtonHandle() throws IOException {
        Utilizator updatedUser = usersTableView.selectionModelProperty().get().getSelectedItem();
        if(updatedUser != null) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-requests-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cereri User");
            UserRequestsController userRequestsController = fxmlLoader.getController();
            userRequestsController.setUser(updatedUser);
            userRequestsController.setServiceCerere(serviceCerere, servicePrietenie, serviceUtilizator);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void messagesButtonHandle() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-messages-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mesaje");
        UserMessagesController userMessagesController = fxmlLoader.getController();
        userMessagesController.setServiceMessages(serviceMessage,serviceUtilizator);
        stage.setScene(scene);
        stage.show();
    }

    private void handlePageNavigationChecks(){
        previousButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage+1)*numberOfRecordsPerPage >= totalNumberOfElements);
    }

    public void goToPreviousPageHandle() throws IOException {
        currentPage--;
        initData();
    }

    public void goToNextPageHandle() throws IOException {
        currentPage++;
        initData();
    }


    private void handleRecordsPerPageChecks(){
        lessButton.setDisable(numberOfRecordsPerPage == 1);
        moreButton.setDisable(numberOfRecordsPerPage == totalNumberOfElements);
    }

    public void moreRecordsPerPageHandle() throws IOException {
        numberOfRecordsPerPage++;
        initData();
    }

    public void lessRecordsPerPageHandle() throws IOException {
        numberOfRecordsPerPage--;
        initData();
    }


}
