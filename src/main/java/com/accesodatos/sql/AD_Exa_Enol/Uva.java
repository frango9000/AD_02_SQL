/*
create table uvas (
tipo varchar2(1),
nomeu varchar2(10),
acidezmin integer,
acidezmax integer,
primary key (tipo)
);
 */
package com.accesodatos.sql.AD_Exa_Enol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Uva {

    private char tipo;
    private String nomeu;
    private int acidezmin;
    private int acidezmax;

    public Uva() {
    }

    public Uva(char tipo, String nomeu, int acidezmin, int acidezmax) {
        this.tipo      = tipo;
        this.nomeu     = nomeu;
        this.acidezmin = acidezmin;
        this.acidezmax = acidezmax;
    }

    public static Uva build(ResultSet rs) throws SQLException {
        Uva uva = new Uva();
        uva.setTipo(rs.getString(1).charAt(0));
        uva.setNomeu(rs.getString(2));
        uva.setAcidezmin(rs.getInt(3));
        uva.setAcidezmax(rs.getInt(4));
        return uva;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getNomeu() {
        return nomeu;
    }

    public void setNomeu(String nomeu) {
        this.nomeu = nomeu;
    }

    public int getAcidezmin() {
        return acidezmin;
    }

    public void setAcidezmin(int acidezmin) {
        this.acidezmin = acidezmin;
    }

    public int getAcidezmax() {
        return acidezmax;
    }

    public void setAcidezmax(int acidezmax) {
        this.acidezmax = acidezmax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Uva uva = (Uva) o;
        return tipo == uva.tipo &&
               acidezmin == uva.acidezmin &&
               acidezmax == uva.acidezmax &&
               Objects.equals(nomeu, uva.nomeu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, nomeu, acidezmin, acidezmax);
    }

    @Override
    public String toString() {
        return "Uva{" +
               "tipo=" + tipo +
               ", nomeu='" + nomeu + '\'' +
               ", acidezmin=" + acidezmin +
               ", acidezmax=" + acidezmax +
               '}';
    }


}
