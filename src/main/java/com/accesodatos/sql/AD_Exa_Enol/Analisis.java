package com.accesodatos.sql.AD_Exa_Enol;

import java.util.Objects;

public class Analisis {

    private String codigoa;
    private int acidez;
    private int grao;
    private int taninos;
    private char tipo;
    private int cantidad;
    private String dni;

    private Cliente cliente;
    private Uva uva;


    public Analisis() {
    }

    public String getCodigoa() {
        return codigoa;
    }

    public void setCodigoa(String codigoa) {
        this.codigoa = codigoa;
    }

    public int getAcidez() {
        return acidez;
    }

    public void setAcidez(int acidez) {
        this.acidez = acidez;
    }

    public int getGrao() {
        return grao;
    }

    public void setGrao(int grao) {
        this.grao = grao;
    }

    public int getTaninos() {
        return taninos;
    }

    public void setTaninos(int taninos) {
        this.taninos = taninos;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Uva getUva() {
        return uva;
    }

    public void setUva(Uva uva) {
        this.uva = uva;
    }

    public void findUva() {
        uva = DataAccessObject.getDAO().getUva(getTipo());
    }

    public void findCliente() {
        cliente = DataAccessObject.getDAO().getCliente(getDni());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Analisis analisis = (Analisis) o;
        return getAcidez() == analisis.getAcidez() &&
               getGrao() == analisis.getGrao() &&
               getTaninos() == analisis.getTaninos() &&
               getTipo() == analisis.getTipo() &&
               getCantidad() == analisis.getCantidad() &&
               Objects.equals(getCodigoa(), analisis.getCodigoa()) &&
               Objects.equals(getDni(), analisis.getDni());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigoa(), getAcidez(), getGrao(), getTaninos(), getTipo(), getCantidad(), getDni());
    }

    @Override
    public String toString() {
        return "Analisis{" +
               "codigoa='" + codigoa + '\'' +
               ", acidez=" + acidez +
               ", grao=" + grao +
               ", taninos=" + taninos +
               ", tipo=" + tipo +
               ", cantidad=" + cantidad +
               ", dni='" + dni + '\'' +
               ", cliente=" + cliente +
               ", uva=" + uva +
               '}';
    }

    public static Analisis build(String[] field) {
        Analisis analisis = new Analisis();
        analisis.setCodigoa(field[0]);
        analisis.setAcidez(Integer.parseInt(field[1]));
        analisis.setGrao(Integer.parseInt(field[2]));
        analisis.setTaninos(Integer.parseInt(field[3]));
        analisis.setTipo(field[4].charAt(0));
        analisis.setCantidad(Integer.parseInt(field[5]));
        analisis.setDni(field[6]);
        return analisis;
    }

    public Xerado produceXerado() {
        Xerado xerado = new Xerado();
        xerado.setNomeuva(getUva().getNomeu());
        xerado.setNum(getCodigoa());
        xerado.setTrataacidez(procesarAcidez());
        xerado.setTotal(calcularTotal());
        return xerado;
    }

    private String procesarAcidez() {
        if (getAcidez() < uva.getAcidezmin())
            return "subir acidez";
        else if (getAcidez() > uva.getAcidezmax())
            return "bajar acidez";
        else
            return "acidez correcta";
    }

    private int calcularTotal() {
        return getCantidad() * 15;
    }


}
