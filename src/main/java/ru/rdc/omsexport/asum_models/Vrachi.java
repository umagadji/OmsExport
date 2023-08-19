package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vrachi {
    private String kod;
    private String fio;
    private String mcod;
    private String idmsp;
    private String spec;
    private String dost;
    private String type;
    private String vers_spec;
    private String ss;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vrachi vrachi = (Vrachi) obj;

        return getKod().equals(vrachi.getKod())
                && getFio().equals(vrachi.getFio())
                && getIdmsp().equals(vrachi.getIdmsp());
    }

}