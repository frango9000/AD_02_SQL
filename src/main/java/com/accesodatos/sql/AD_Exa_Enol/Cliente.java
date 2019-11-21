/*
create table clientes(
dni varchar2(9),
nome varchar2(15),
telf integer,
numerodeanalisis integer
);
 */
package com.accesodatos.sql.AD_Exa_Enol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Cliente {

    private String dni;
    private String nome;
    private int telf;
    private int numerodeanalisis;

    public Cliente() {
    }

    public Cliente(String dni, String nome, int telf, int numerodeanalisis) {
        this.dni              = dni;
        this.nome             = nome;
        this.telf             = telf;
        this.numerodeanalisis = numerodeanalisis;
    }

    public static Cliente build(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setDni(rs.getString(1));
        cliente.setNome(rs.getString(2));
        cliente.setTelf(rs.getInt(3));
        cliente.setNumerodeanalisis(rs.getInt(4));
        return cliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTelf() {
        return telf;
    }

    public void setTelf(int telf) {
        this.telf = telf;
    }

    public int getNumerodeanalisis() {
        return numerodeanalisis;
    }

    public void setNumerodeanalisis(int numerodeanalisis) {
        this.numerodeanalisis = numerodeanalisis;
    }

    public void incrementarNumerodeanalisis() {
        this.numerodeanalisis++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cliente cliente = (Cliente) o;
        return getTelf() == cliente.getTelf() &&
               getNumerodeanalisis() == cliente.getNumerodeanalisis() &&
               Objects.equals(getDni(), cliente.getDni()) &&
               Objects.equals(getNome(), cliente.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDni(), getNome(), getTelf(), getNumerodeanalisis());
    }

    @Override
    public String toString() {
        return "Cliente{" +
               "dni='" + dni + '\'' +
               ", nome='" + nome + '\'' +
               ", telf=" + telf +
               ", numerodeanalisis=" + numerodeanalisis +
               '}';
    }
}
