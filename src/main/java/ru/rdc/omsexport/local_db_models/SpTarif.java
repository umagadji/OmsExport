package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "sp_tarif")
public class SpTarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name_issl;
    @Column
    private String ksg;
    @Column
    private double price;
    @Column
    private int type;
    @Column
    private int idpr;
    @Column
    private int idump;
    @Column
    private double kol_usl;
    @Column
    private int t_type;
    @Column
    private int idvmp;
    @Column
    private String idpc;

    @Transient
    private boolean ex_7_2;

    public boolean isEx_7_2() {
        return ex_7_2;
    }

    public void setEx_7_2(boolean ex_7_2) {
        this.ex_7_2 = ex_7_2;
    }

    /*@OneToOne
    @JoinColumn(name = "sp_tarif_add", referencedColumnName = "ksg")
    private SpTarifAdd spTarifAdd;*/

    /*public SpTarifAdd getSpTarifAdd() {
        return spTarifAdd;
    }

    public void setSpTarifAdd(SpTarifAdd spTarifAdd) {
        this.spTarifAdd = spTarifAdd;
    }*/

    /*@OneToOne
    @JoinColumn(name = "ksg_code", referencedColumnName = "ksgcode")
    private KsgCode ksgCode;*/

    public SpTarif() {
    }

    /*public KsgCode getKsgCode() {
        return ksgCode;
    }

    public void setKsgCode(KsgCode ksgCode) {
        this.ksgCode = ksgCode;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_issl() {
        return name_issl;
    }

    public void setName_issl(String name_issl) {
        this.name_issl = name_issl;
    }

    public String getKsg() {
        return ksg;
    }

    public void setKsg(String ksg) {
        this.ksg = ksg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdpr() {
        return idpr;
    }

    public void setIdpr(int idpr) {
        this.idpr = idpr;
    }

    public int getIdump() {
        return idump;
    }

    public void setIdump(int idump) {
        this.idump = idump;
    }

    public double getKol_usl() {
        return kol_usl;
    }

    public void setKol_usl(double kol_usl) {
        this.kol_usl = kol_usl;
    }

    public int getT_type() {
        return t_type;
    }

    public void setT_type(int t_type) {
        this.t_type = t_type;
    }

    public int getIdvmp() {
        return idvmp;
    }

    public void setIdvmp(int idvmp) {
        this.idvmp = idvmp;
    }

    public String getIdpc() {
        return idpc;
    }

    public void setIdpc(String idpc) {
        this.idpc = idpc;
    }

    @Override
    public String toString() {
        return "SpTarif{" +
                "id=" + id +
                ", name_issl='" + name_issl + '\'' +
                ", ksg='" + ksg + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", idpr=" + idpr +
                ", idump=" + idump +
                ", kol_usl=" + kol_usl +
                ", t_type=" + t_type +
                ", idvmp=" + idvmp +
                ", idpc='" + idpc + '\'' +
                '}';
    }
}
