package com.example.toysocialnetworkgui.domain.validators;

import com.example.toysocialnetworkgui.domain.Prietenie;
import com.example.toysocialnetworkgui.domain.Utilizator;

public class PrietenieValidator implements Validator<Prietenie>{

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getFirst() == entity.getSecond())
            throw new ValidationException("Prietenia are nevoie de doi utilizatori!") ;
    }
}
