package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mkb_extended")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MkbExtended {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String lcod;
    @Column
    private int terr;
    @Column
    private boolean is_onk;

    public MkbExtended(String lcod, int terr, boolean is_onk) {
        this.lcod = lcod;
        this.terr = terr;
        this.is_onk = is_onk;
    }

    @Override
    public String toString() {
        return "MkbExtended{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                ", terr=" + terr +
                ", is_onk=" + is_onk +
                '}';
    }
}
