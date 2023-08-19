package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "smo")
public class Smo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String tf_okato;
    @Column
    private String smocod;
    @Column
    private String nam_smok;
    @Column
    private String ogrn;

    public Smo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTf_okato() {
        return tf_okato;
    }

    public void setTf_okato(String tf_okato) {
        this.tf_okato = tf_okato;
    }

    public String getSmocod() {
        return smocod;
    }

    public void setSmocod(String smocod) {
        this.smocod = smocod;
    }

    public String getNam_smok() {
        return nam_smok;
    }

    public void setNam_smok(String nam_smok) {
        this.nam_smok = nam_smok;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    @Override
    public String toString() {
        return "Smo{" +
                "id=" + id +
                ", tf_okato='" + tf_okato + '\'' +
                ", smocod='" + smocod + '\'' +
                ", nam_smok='" + nam_smok + '\'' +
                ", ogrn='" + ogrn + '\'' +
                '}';
    }
}
