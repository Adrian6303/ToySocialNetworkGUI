package com.example.toysocialnetworkgui.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
