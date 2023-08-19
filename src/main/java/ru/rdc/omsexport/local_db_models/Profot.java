package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

import java.time.LocalDate;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "profot")
public class Profot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private int idpr;
    @Column
    private String prname;
    @Column (columnDefinition = "DATE")
    private LocalDate datebeg;
    @Column (columnDefinition = "DATE")
    private LocalDate dateend;
    @Column
    private boolean activ;

    public Profot() {
    }

    public int getIdpr() {
        return idpr;
    }

    public void setIdpr(int idpr) {
        this.idpr = idpr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    public LocalDate getDatebeg() {
        return datebeg;
    }

    public void setDatebeg(LocalDate datebeg) {
        this.datebeg = datebeg;
    }

    public LocalDate getDateend() {
        return dateend;
    }

    public void setDateend(LocalDate dateend) {
        this.dateend = dateend;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    @Override
    public String toString() {
        return "Profot{" +
                "id=" + id +
                ", idpr=" + idpr +
                ", prname='" + prname + '\'' +
                ", datebeg=" + datebeg +
                ", dateend=" + dateend +
                ", activ=" + activ +
                '}';
    }
}
