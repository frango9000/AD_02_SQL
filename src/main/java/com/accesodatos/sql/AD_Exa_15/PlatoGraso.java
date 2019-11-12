package com.accesodatos.sql.AD_Exa_15;

public class PlatoGraso extends Platos {

    int grasa = 0;


    public PlatoGraso() {
    }

    public PlatoGraso(int grasa) {
        this.grasa = grasa;
    }

    public PlatoGraso(String codigo, String nome, int grasa) {
        super(codigo, nome);
        this.grasa = grasa;
    }

    public int getGrasa() {
        return grasa;
    }

    public void setGrasa(int grasa) {
        this.grasa = grasa;
    }

    public String toString() {
        return "codigo plato : " + getCodigop() + "\n" +
               "nome plato  : " + getNomep() + "\n" +
               "graxa plato  : " + getGrasa() + "\n";
    }

}
