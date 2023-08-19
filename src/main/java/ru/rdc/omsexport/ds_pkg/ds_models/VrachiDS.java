package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс описывающий врача
public class VrachiDS {
    private String kod;
    private String fio;
    private String mcod;
    private String idmsp;
    private String spec;
    private String dost;
    private String type;
    private String vers_spec;
    private String ss;

    public VrachiDS() {}

    public VrachiDS(String kod, String fio, String mcod, String idmsp, String spec, String dost, String type, String vers_spec, String ss) {
        this.kod = kod;
        this.fio = fio;
        this.mcod = mcod;
        this.idmsp = idmsp;
        this.spec = spec;
        this.dost = dost;
        this.type = type;
        this.vers_spec = vers_spec;
        this.ss = ss;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getMcod() {
        return mcod;
    }

    public void setMcod(String mcod) {
        this.mcod = mcod;
    }

    public String getIdmsp() {
        return idmsp;
    }

    public void setIdmsp(String idmsp) {
        this.idmsp = idmsp;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDost() {
        return dost;
    }

    public void setDost(String dost) {
        this.dost = dost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVers_spec() {
        return vers_spec;
    }

    public void setVers_spec(String vers_spec) {
        this.vers_spec = vers_spec;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    @Override
    public String toString() {
        return "Vrachi{" +
                "kod='" + kod + '\'' +
                ", fio='" + fio + '\'' +
                ", mcod='" + mcod + '\'' +
                ", idmsp='" + idmsp + '\'' +
                ", spec='" + spec + '\'' +
                ", dost='" + dost + '\'' +
                ", type='" + type + '\'' +
                ", vers_spec='" + vers_spec + '\'' +
                ", ss='" + ss + '\'' +
                '}';
    }
}
