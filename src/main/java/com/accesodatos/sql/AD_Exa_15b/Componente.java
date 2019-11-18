package com.accesodatos.sql.AD_Exa_15b;

import java.io.Serializable;
import java.util.Objects;

public class Componente implements Serializable {

    String codc;
    String nomec;
    int graxaPercent;

    public Componente() {
    }

    public Componente(String codc) {
        this.codc = codc;
    }

    public Componente(String codc, String nomec, int graxaPercent) {
        this.codc         = codc;
        this.nomec        = nomec;
        this.graxaPercent = graxaPercent;
    }

    public String getCodc() {
        return codc;
    }

    public void setCodc(String codc) {
        this.codc = codc;
    }

    public String getNomec() {
        return nomec;
    }

    public void setNomec(String nomec) {
        this.nomec = nomec;
    }

    public int getGraxaPercent() {
        return graxaPercent;
    }

    public void setGraxaPercent(int graxaPercent) {
        this.graxaPercent = graxaPercent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Componente that = (Componente) o;
        return getGraxaPercent() == that.getGraxaPercent() &&
               Objects.equals(getCodc(), that.getCodc()) &&
               Objects.equals(getNomec(), that.getNomec());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodc(), getNomec(), getGraxaPercent());
    }


    @Override
    public String toString() {
        return "Componente{" +
               "codc='" + codc + '\'' +
               ", nomec='" + nomec + '\'' +
               ", graxa=" + graxaPercent +
               '}';
    }
}
