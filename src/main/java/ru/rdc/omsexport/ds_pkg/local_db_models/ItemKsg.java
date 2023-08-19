package ru.rdc.omsexport.ds_pkg.local_db_models;

public class ItemKsg {
    private String ksg;

    public ItemKsg() {
    }

    public String getKsg() {
        return ksg;
    }

    public void setKsg(String ksg) {
        this.ksg = ksg;
    }

    @Override
    public String toString() {
        return "ItemKsg{" +
                "ksg='" + ksg + '\'' +
                '}';
    }
}
