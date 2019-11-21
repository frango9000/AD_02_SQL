/*
create table xerado(
num varchar2(4),
nomeuva varchar2(15),
tratacidez varchar2(15),
total integer,
primary key (num)
);

 */
package com.accesodatos.sql.AD_Exa_Enol;

import java.util.Objects;

public class Xerado {

    private String num;
    private String nomeuva;
    private String trataacidez;
    private int total;

    public Xerado() {
    }

    public Xerado(String num, String nomeuva, String trataacidez, int total) {
        this.num         = num;
        this.nomeuva     = nomeuva;
        this.trataacidez = trataacidez;
        this.total       = total;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNomeuva() {
        return nomeuva;
    }

    public void setNomeuva(String nomeuva) {
        this.nomeuva = nomeuva;
    }

    public String getTrataacidez() {
        return trataacidez;
    }

    public void setTrataacidez(String trataacidez) {
        this.trataacidez = trataacidez;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Xerado xerado = (Xerado) o;
        return getTotal() == xerado.getTotal() &&
               Objects.equals(getNum(), xerado.getNum()) &&
               Objects.equals(getNomeuva(), xerado.getNomeuva()) &&
               Objects.equals(getTrataacidez(), xerado.getTrataacidez());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNum(), getNomeuva(), getTrataacidez(), getTotal());
    }

    @Override
    public String toString() {
        return "Xerado{" +
               "num='" + num + '\'' +
               ", nomeuva='" + nomeuva + '\'' +
               ", trataacidez='" + trataacidez + '\'' +
               ", total=" + total +
               '}';
    }
}
