package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;


public class Prietenie extends com.example.toysocialnetworkgui.domain.Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Prietenie(long ID1, long ID2, LocalDateTime date) {
        super.id = new Tuple<Long, Long>(ID1, ID2);
        this.date = date;
    }

    public Tuple<Long,Long> getPrietenie(){
        return this.id;
    }
    public Long getFirst(){
        return getId().getLeft();
    }

    public Long getSecond(){
        return getId().getRight();
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "primul id " + id.getLeft()+ " | " +
                "al doilea id " +  id.getRight();
    }
}

