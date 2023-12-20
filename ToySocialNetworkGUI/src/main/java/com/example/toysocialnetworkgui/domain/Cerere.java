package com.example.toysocialnetworkgui.domain;

public class Cerere extends Entity<Tuple<Long,Long>> {
    private String status;

    public Cerere(long id1, long id2){
        super.id = new Tuple<Long, Long>(id1, id2);
        this.status = "in asteptare";
    }

    public Tuple<Long,Long> getCerere(){
        return this.id;
    }
    public Long getFirst(){
        return getId().getLeft();
    }
    public Long getSecond(){
        return getId().getRight();
    }

    public String getStatus(){return status;}

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "primul id " + id.getLeft()+ " | " +
                "al doilea id " +  id.getRight() +
                " | " + status;
    }
}
