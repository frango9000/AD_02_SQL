package com.accesodatos.sql.AD_19_baserelacionalB;

import com.accesodatos.sql.AD_18_baserelacionalA.Producto;
import com.accesodatos.sql.AD_18_baserelacionalA.ProductosDao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductosDaoNav extends ProductosDao {

    protected static ProductosDaoNav productoDao;

    public static ProductosDaoNav getSession() {
        if (productoDao == null) {
            synchronized (ProductosDao.class) {
                if (productoDao == null) {
                    productoDao = new ProductosDaoNav();
                }
            }
        }
        return productoDao;
    }


    public int actualizar(String id, int precio) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT t.* FROM %s t", TABLE_NAME);
//            System.out.println(sessionDB.getConn().getMetaData().);
            Connection connection = sessionDB.getConn();
            try (
                Statement ps = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = ps.executeQuery(sql)) {
                while (rs.next()) {
                    Producto producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    System.out.println(producto.toString());
                    if (producto.getId().equals(id)) {
                        rs.updateInt("precio", precio);
                        rs.updateRow();
                        rows++;
                    }
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    public int deleteEnResultSet(String id) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT t.* FROM %s t", TABLE_NAME);
//            System.out.println(sessionDB.getConn().getMetaData().);
            Connection connection = sessionDB.getConn();
            try (
                Statement ps = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = ps.executeQuery(sql)) {
                while (rs.next()) {
                    Producto producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    System.out.println(producto.toString());
                    if (producto.getId().equals(id)) {
                        rs.deleteRow();
                    }
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    public int insertEnResultSet(Producto p4) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT t.* FROM %s t", TABLE_NAME);
//            System.out.println(sessionDB.getConn().getMetaData().);
            Connection connection = sessionDB.getConn();
            try (Statement ps = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = ps.executeQuery(sql)) {
                System.out.println("Result Set: ");
                while (rs.next()) {
                    Producto producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    System.out.println(producto.toString());
                }

                System.out.println("Insert " + p4.toString());
                rs.moveToInsertRow();
                rs.updateString("codigo", p4.getId());
                rs.updateString("descripcion", p4.getDescripcion());
                rs.updateInt("precio", p4.getPrecio());
                rs.insertRow();

                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }
}
