package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private Long id_from;
    private Long id_to;
    private String message;
    private LocalDateTime data;

    public Message(Long id_from,Long id_to ) {
        this.id_from = id_from;
        this.id_to = id_to;
    }

    public Long getId_from() {
        return id_from;
    }

    public void setId_from(Long id_from) {
        this.id_from = id_from;
    }

    public Long getId_to() {
        return id_to;
    }

    public void setId_to(Long id_to) {
        this.id_to = id_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

}
