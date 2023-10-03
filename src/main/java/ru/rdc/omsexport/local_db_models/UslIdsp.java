package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность usl_idsp. Начиная с 09.2023 ТФОМС ввел проверку на соответствие услуги способу оплаты
@Entity
@Table(name = "usl_idsp")
public class UslIdsp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String ksg;

    @Column
    private int vid_usl;

    @Column
    private String name_vid;

    @Column
    private String idsp_name;

    @Column
    private int idsp;

    public UslIdsp() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKsg() {
        return ksg;
    }

    public void setKsg(String ksg) {
        this.ksg = ksg;
    }

    public int getVid_usl() {
        return vid_usl;
    }

    public void setVid_usl(int vid_usl) {
        this.vid_usl = vid_usl;
    }

    public String getName_vid() {
        return name_vid;
    }

    public void setName_vid(String name_vid) {
        this.name_vid = name_vid;
    }

    public String getIdsp_name() {
        return idsp_name;
    }

    public void setIdsp_name(String idsp_name) {
        this.idsp_name = idsp_name;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    @Override
    public String toString() {
        return "UslIdsp{" +
                "id=" + id +
                ", ksg='" + ksg + '\'' +
                ", vid_usl=" + vid_usl +
                ", name_vid='" + name_vid + '\'' +
                ", idsp_name='" + idsp_name + '\'' +
                ", idsp=" + idsp +
                '}';
    }
}
