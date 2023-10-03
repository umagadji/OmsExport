package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Этот класс описывает таблицу в БД, которая будет сормирована будем запроса ниже
/*
SELECT name_issl, sp_tarif.ksg, price, sp_tarif.type, sp_tarif.idpr, kol_usl, t_type, sp_tarif_add.ex_7_2, prname, IIF(ISNULL(usl_kr_multi.ksg), 0, 1) kr_mul
FROM sp_tarif
INNER JOIN sp_tarif_add ON sp_tarif.type=1 AND (kol_usl=1 OR kol_usl=1.92) AND sp_tarif.ksg=sp_tarif_add.ksg
INNER JOIN profot ON sp_tarif.idpr = profot.idpr
LEFT JOIN usl_kr_multi ON ALLTRIM(sp_tarif.ksg) = ALLTRIM(usl_kr_multi.ksg)
 */
//Расширенная версия SpTarif с полями из sp_tarif, sp_tarif_add, profot, usl_kr_multi
@Entity
@Table(name = "sp_tarif_extended")
public class SpTarifExtended {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private double kol_usl;
    @Column
    private int t_type;
    @Column
    private boolean ex_7_2;
    @Column
    private String prname;
    @Column
    private int kr_mul; //0, если нельзя несколько раз подавать услугу на оплату, 1 - если можно
    @Column
    private int max_krat; //максимальная кратность услуги
    @Column
    private int usl_idsp; //Способ оплаты согласно обновлениям ТФОМС от 09.2023, используя таблицу usl_idsp из локальной БД
    @Column
    private boolean muvr;

    public SpTarifExtended() {
    }

    public SpTarifExtended(String name_issl, String ksg, double price, int type, int idpr, double kol_usl, int t_type, boolean ex_7_2, String prname, int kr_mul, int max_krat, int usl_idsp, boolean muvr) {
        this.name_issl = name_issl;
        this.ksg = ksg;
        this.price = price;
        this.type = type;
        this.idpr = idpr;
        this.kol_usl = kol_usl;
        this.t_type = t_type;
        this.ex_7_2 = ex_7_2;
        this.prname = prname;
        this.kr_mul = kr_mul;
        this.max_krat = max_krat;
        this.usl_idsp = usl_idsp;
        this.muvr = muvr;
    }

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

    public boolean isEx_7_2() {
        return ex_7_2;
    }

    public void setEx_7_2(boolean ex_7_2) {
        this.ex_7_2 = ex_7_2;
    }

    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    public int getKr_mul() {
        return kr_mul;
    }

    public void setKr_mul(int kr_mul) {
        this.kr_mul = kr_mul;
    }

    public int getMax_krat() {
        return max_krat;
    }

    public void setMax_krat(int max_krat) {
        this.max_krat = max_krat;
    }

    public int getUsl_idsp() {
        return usl_idsp;
    }

    public void setUsl_idsp(int usl_idsp) {
        this.usl_idsp = usl_idsp;
    }

    public boolean isMuvr() {
        return muvr;
    }

    public void setMuvr(boolean muvr) {
        this.muvr = muvr;
    }

    @Override
    public String toString() {
        return "SpTarifNew{" +
                "id=" + id +
                ", name_issl='" + name_issl + '\'' +
                ", ksg='" + ksg + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", idpr=" + idpr +
                ", kol_usl=" + kol_usl +
                ", t_type=" + t_type +
                ", ex_7_2=" + ex_7_2 +
                ", prname='" + prname + '\'' +
                ", kr_mul='" + kr_mul + '\'' +
                ", max_krat='" + max_krat + '\'' +
                ", usl_idsp='" + usl_idsp + '\'' +
                ", muvr='" + muvr + '\'' +
                '}';
    }
}
