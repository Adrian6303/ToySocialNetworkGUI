package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.Utilizator;

import java.util.ArrayList;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String errors = "";
        if(entity.getFirstName().length()==0 || entity.getFirstName().length()>100)
            errors+="Prenume invalid!\n";
        if(entity.getLastName().length()==0 )
            errors+="Nume invalid!\n";
        if(errors.length()>1)
            throw new ValidationException(errors);
    }
}
