/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accesodatos.sql.misc.data;

import com.accesodatos.sql.misc.model.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author NarF
 */
public class ProductosDao extends AbstractDao<String, Producto> {

    /**
     * Singleton lazy initialization
     */
    protected static ProductosDao productoDao;

    protected ProductosDao() {
        TABLE_NAME  = "productos";
        ID_COL_NAME = "codigo";
    }

    public static ProductosDao getSession() {
        if (productoDao == null) {
            synchronized (ProductosDao.class) {
                if (productoDao == null) {
                    productoDao = new ProductosDao();
                }
            }
        }
        return productoDao;
    }

    @Override
    public Producto query(String id) {
        Producto producto = null;
        if (sessionDB.connect()) {
            String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, ID_COL_NAME, id);
            try (Statement ps = sessionDB.getConn().createStatement();
                 ResultSet rs = ps.executeQuery(sql)) {
                if (rs.next()) {
                    producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    table.put(producto.getId(), producto);
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return producto;
    }

    @Override
    public HashMap<String, Producto> query(ArrayList<String> ids) {
        HashMap<String, Producto> productosTemp = new HashMap<>();
        if (sessionDB.connect() && ids.size() > 0) {
            StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COL_NAME + " IN( 0");
            for (String id : ids) {
                sql.append(", ").append(id);
            }
            sql.append(" )");
            try (Statement ps = sessionDB.getConn().createStatement();
                 ResultSet rs = ps.executeQuery(sql.toString())) {
                while (rs.next()) {
                    Producto producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    table.put(producto.getId(), producto);
                    productosTemp.put(producto.getId(), producto);
                }
                printSql(sql.toString());
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql.toString(), ex);
            } finally {
                sessionDB.close();
            }
        }
        return productosTemp;
    }

    @Override
    public HashMap<String, Producto> queryAll() {
        table.clear();
        if (sessionDB.connect()) {
            String sql = String.format("SELECT * FROM %s", TABLE_NAME);
            try (Statement ps = sessionDB.getConn().createStatement();
                 ResultSet rs = ps.executeQuery(sql)) {
                while (rs.next()) {
                    Producto producto = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3));
                    table.put(producto.getId(), producto);
                }
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return table;
    }

    @Override
    public int insert(Producto producto) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("INSERT INTO %s VALUES(?, ?, ?)", TABLE_NAME);
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setString(1, producto.getId());
                pstmt.setString(2, producto.getDescripcion());
                pstmt.setInt(3, producto.getPrecio());
                rows = pstmt.executeUpdate();

                table.put(producto.getId(), producto);
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    @Override
    public int update(Producto producto) {
        int rows = 0;
        if (sessionDB.connect()) {
            String sql = String.format("UPDATE %s SET descripcion = ?, precio = ? WHERE %s = ?", TABLE_NAME, ID_COL_NAME);
            try (PreparedStatement pstmt = sessionDB.getConn().prepareStatement(sql)) {
                pstmt.setString(1, producto.getDescripcion());
                pstmt.setFloat(2, producto.getPrecio());
                pstmt.setString(3, producto.getId());
                rows = pstmt.executeUpdate();
                printSql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
            } finally {
                sessionDB.close();
            }
        }
        return rows;
    }

    @Override
    public int updateDao(Producto producto) {
        int rows = 0;
        if (producto.getId().length() > 0) {
            if (sessionDB.connect()) {
                String sql = String.format("SELECT * FROM %s WHERE %s = '%d'", TABLE_NAME, ID_COL_NAME, producto.getId());
                try (Statement ps = sessionDB.getConn().createStatement();
                     ResultSet rs = ps.executeQuery(sql)) {
                    if (rs.next()) {
                        producto.setDescripcion(rs.getString(2));
                        producto.setPrecio(rs.getInt(3));
                        rows++;
                        table.put(producto.getId(), producto);
                    }
                    printSql(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(ProductosDao.class.getName()).log(Level.SEVERE, sql, ex);
                } finally {
                    sessionDB.close();
                }
            }
        }
        return rows;
    }

}
