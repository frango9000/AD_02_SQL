package com.accesodatos.sql.misc.model;

import com.accesodatos.sql.misc.data.AbstractDao;
import com.accesodatos.sql.misc.data.ProductosDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Producto implements IPersistable<String>, Serializable {

    private String codigo;
    private String descripcion;
    private int precio = 0;

    public Producto() {
    }

    public Producto(String codigo, String descripcion, int precio) {
        this.codigo      = codigo;
        this.descripcion = descripcion;
        this.precio      = precio;
    }

    @Override
    public String getId() {
        return codigo;
    }

    @Override
    public AbstractDao<String, Producto> getDataStore() {
        return ProductosDao.getSession();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Producto producto = (Producto) o;

        if (precio != producto.precio)
            return false;
        if (codigo != null ? !codigo.equals(producto.codigo) : producto.codigo != null)
            return false;
        return descripcion != null ? descripcion.equals(producto.descripcion) : producto.descripcion == null;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + precio;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Producto.class.getSimpleName() + "[", "]")
            .add("codigo='" + codigo + "'")
            .add("descripcion='" + descripcion + "'")
            .add("precio=" + precio)
            .toString();
    }

    public static ArrayList<Producto> generateProducts() {
        ArrayList<Producto> products = new ArrayList<>();
        String[] cod = {"p1", "p2", "p3"};
        String[] desc = {"parafusos", "cravos", "tachas"};
        int[] prezo = {3, 4, 5};
        for (int i = 0; i < cod.length; i++) {
            products.add(new Producto(cod[i], desc[i], prezo[i]));
        }
        return products;
    }

}
