package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private String prname;
    @Column
    private int kr_mul; //0, если нельзя несколько раз подавать услугу на оплату, 1 - если можно
    @Column
    private int max_krat; //максимальная кратность услуги
    @Column
    private boolean muvr;
    @Column
    private int usl_idsp; //Способ оплаты согласно обновлениям ТФОМС от 09.2023, используя таблицу usl_idsp из локальной БД

    public SpTarifExtended(String name_issl, String ksg, double price, int type, int idpr, double kol_usl, int t_type, String prname, int kr_mul, int max_krat, boolean muvr, int usl_idsp) {
        this.name_issl = name_issl;
        this.ksg = ksg;
        this.price = price;
        this.type = type;
        this.idpr = idpr;
        this.kol_usl = kol_usl;
        this.t_type = t_type;
        this.prname = prname;
        this.kr_mul = kr_mul;
        this.max_krat = max_krat;
        this.muvr = muvr;
        this.usl_idsp = usl_idsp;
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
                ", prname='" + prname + '\'' +
                ", kr_mul='" + kr_mul + '\'' +
                ", max_krat='" + max_krat + '\'' +
                ", muvr='" + muvr + '\'' +
                ", usl_idsp='" + usl_idsp + '\'' +
                '}';
    }
}
