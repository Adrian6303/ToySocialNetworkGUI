package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.Cerere;

public class CerereValidator implements Validator<Cerere> {

    @Override
    public void validate(Cerere entity) throws ValidationException {
        if(entity.getStatus()!= "in asteptare" && entity.getStatus()!= "acceptata" && entity.getStatus()!= "respinsa")
            throw new ValidationException("Status invalid!") ;
        if(entity.getFirst() == entity.getSecond())
            throw new ValidationException("Cerere invalida!");
    }
}
