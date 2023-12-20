package com.example.toysocialnetworkgui.controller;

import com.example.toysocialnetworkgui.domain.Utilizator;
import com.example.toysocialnetworkgui.service.ServiceUtilizator;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditController {
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    private ServiceUtilizator serviceUtilizator;
    private Utilizator utilizator = null;

    public void setServiceUtilizator(ServiceUtilizator serviceUtilizator) {
        this.serviceUtilizator = serviceUtilizator;
    }

    public void setUtilizator(Utilizator utilizator)
    {
        this.utilizator = utilizator;
        if(utilizator != null)
        {
            firstNameTextField.setText(utilizator.getFirstName());
            lastNameTextField.setText(utilizator.getLastName());
        }
    }

    public void ConfirmButtonClick() throws Throwable {
        if(utilizator == null)
        {
            serviceUtilizator.save(new Utilizator(firstNameTextField.getText(), lastNameTextField.getText()));
        }
        else
        {
            Utilizator updated = new Utilizator(firstNameTextField.getText(), lastNameTextField.getText());
            updated.setID(utilizator.getId());
            serviceUtilizator.update(updated);
        }
    }

    public void ExitButtonClick() {
        Stage stage = (Stage) firstNameTextField.getScene().getWindow();
        stage.close();
    }
}
